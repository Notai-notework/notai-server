package notai.domain.gemini.service;

import lombok.RequiredArgsConstructor;
import notai.domain.gemini.dto.GeminiRequestDTO;
import notai.domain.gemini.dto.GeminiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api.secret}")
    private String secret;

    @Value("${gemini.api.url}")
    private String url;

    private final RestTemplate restTemplate;

    // Gemini 요청 및 응답
    public String callGemini(String question) {

        String requestURL = url + "?key=" + secret;

        GeminiRequestDTO request = new GeminiRequestDTO(question + ". 답은 한국어로 해줘.");

        GeminiResponseDTO response = restTemplate.postForObject(requestURL, request,
            GeminiResponseDTO.class);

        if (response == null) {
            throw new RuntimeException("Gemini 요청 에러");
        }

        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }
}
