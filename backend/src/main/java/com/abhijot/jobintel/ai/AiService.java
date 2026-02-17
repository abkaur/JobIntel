package com.abhijot.jobintel.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestClient restClient;
    private final String model;
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiService(
            @Value("${openai.api.key:}") String apiKey,
            @Value("${openai.model:gpt-4.1-mini}") String model
    ) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model;

        RestClient.Builder builder = RestClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        if (!this.apiKey.isBlank()) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.apiKey);
        }

        this.restClient = builder.build();
    }

    public ImproveJobResponse improveJob(ImproveJobRequest req) {
        requireKey();

        String prompt = """
                You are helping a recruiter write a clear, professional job posting.
                Return STRICT JSON with keys:
                improvedDescription (string), keySkills (array of strings).
                
                Job Title: %s
                Job Description: %s
                """.formatted(req.title(), req.description());

        Map<String, Object> json = callOpenAiJson(prompt);

        return new ImproveJobResponse(
                (String) json.getOrDefault("improvedDescription", ""),
                castList(json.get("keySkills"))
        );
    }

    public MatchResumeResponse matchResume(MatchResumeRequest req) {
        requireKey();

        String prompt = """
                You are an ATS evaluator.
                Return STRICT JSON with keys:
                matchScore (0-100),
                missingSkills (array),
                improvementTips (array).
                
                Job Title: %s
                Job Description: %s
                Resume Text: %s
                """.formatted(req.jobTitle(), req.jobDescription(), req.resumeText());

        Map<String, Object> json = callOpenAiJson(prompt);

        int score = json.get("matchScore") instanceof Number n ? n.intValue() : 0;

        return new MatchResumeResponse(
                score,
                castList(json.get("missingSkills")),
                castList(json.get("improvementTips"))
        );
    }

    public SummarizeJobResponse summarizeJob(SummarizeJobRequest req) {
        requireKey();

        String prompt = """
                Summarize job and extract skills.
                Return STRICT JSON with keys:
                bullets (array),
                keySkills (array).
                
                Job Title: %s
                Job Description: %s
                """.formatted(req.title(), req.description());

        Map<String, Object> json = callOpenAiJson(prompt);

        return new SummarizeJobResponse(
                castList(json.get("bullets")),
                castList(json.get("keySkills"))
        );
    }

    private void requireKey() {
        if (apiKey.isBlank()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                    "OPENAI_API_KEY not configured"
            );
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> callOpenAiJson(String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "input", prompt,
                "text", Map.of("format", Map.of("type", "json_object"))
        );

        try {
            Map<String, Object> res = restClient.post()
                    .uri("/responses")
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            List<Map<String, Object>> output = (List<Map<String, Object>>) res.get("output");
            Map<String, Object> first = output.get(0);
            List<Map<String, Object>> content = (List<Map<String, Object>>) first.get("content");

            String jsonText = (String) content.get(0).get("text");

            return objectMapper.readValue(jsonText, Map.class);

        } catch (Exception e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_GATEWAY,
                    "AI error: " + e.getMessage()
            );
        }
    }
    

    private static List<String> castList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
    }
}
