package notai.domain.gemini.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GeminiRequestDTO {
    private Content contents;
    private final GenerationConfig generationConfig = new GenerationConfig();

    public GeminiRequestDTO(String prompt) {
        Part part = new Part(prompt);
        contents = new Content(part);
    }

    @Data
    @AllArgsConstructor
    public static class Content {
        private Part parts;
    }

    @Data
    @AllArgsConstructor
    public static class Part {
        private String text;
    }

    @Data
    public static class GenerationConfig {
        private final Integer maxOutputTokens = 5000; // 응답 토큰 수 제한
        private final Integer candidateCount = 1; // 응답 수
    }
}
