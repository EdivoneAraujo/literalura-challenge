package br.com.alura.literalura.service;

import br.com.alura.literalura.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GutendexService {
    private static final String API_URL = "https://gutendex.com/books/";

    public List<BookDTO> searchBooks(String query) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(API_URL + "?search=" + encodedQuery))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            GutendexResponse gutendexResponse = objectMapper.readValue(jsonResponse, GutendexResponse.class);
            return gutendexResponse.getResults();
        } else {
            throw new RuntimeException("Erro ao buscar livros: " + response.statusCode());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GutendexResponse {
        private List<BookDTO> results;

        public List<BookDTO> getResults() {
            return results;
        }
    }
}