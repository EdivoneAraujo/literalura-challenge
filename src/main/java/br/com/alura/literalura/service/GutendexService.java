package br.com.alura.literalura.service;

import br.com.alura.literalura.dto.BookDTO;
import br.com.alura.literalura.exception.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class GutendexService {

    @Autowired
    private RestTemplate restTemplate;  // Injeção do RestTemplate para fazer as requisições HTTP

    // Método para buscar livros por título ou autor
    public List<BookDTO> searchBooks(String query, int page, int pageSize) throws Exception {
        // Monta a URL da API de maneira segura e codificada
        String url = UriComponentsBuilder.fromHttpUrl("https://gutendex.com/books/")
                .queryParam("search", query)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .toUriString();

        try {
            // Realiza a requisição HTTP para obter os livros
            String response = restTemplate.getForObject(url, String.class);

            // Processa a resposta JSON para extrair a lista de livros
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode booksNode = root.path("results");

            List<BookDTO> books = new ArrayList<>();
            for (JsonNode node : booksNode) {
                books.add(objectMapper.treeToValue(node, BookDTO.class));  // Converte o nó JSON para BookDTO
            }

            return books;  // Retorna a lista de livros

        } catch (HttpClientErrorException e) {
            // Em caso de erro de cliente (erro HTTP), lança uma exceção personalizada
            throw new ApiException("Erro ao buscar livros: " + e.getMessage(), e.getStatusCode().value());
        } catch (RestClientException e) {
            // Em caso de outro erro do cliente, lança uma exceção personalizada
            throw new ApiException("Erro ao fazer requisição para a API: " + e.getMessage(), 500); // Código 500 para erro genérico
        }
    }

    // Método para buscar livros por idioma
    public List<BookDTO> searchBooksByLanguage(String language, int page, int limit) throws Exception {
        // Monta a URL da API para buscar livros por idioma
        String url = UriComponentsBuilder.fromHttpUrl("https://gutendex.com/books/")
                .queryParam("languages", language)
                .queryParam("page", page)
                .queryParam("page_size", limit)
                .toUriString();

        try {
            // Realiza a requisição HTTP para obter os livros por idioma
            String response = restTemplate.getForObject(url, String.class);

            // Processa a resposta JSON para extrair a lista de livros
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode booksNode = root.path("results");

            List<BookDTO> books = new ArrayList<>();
            for (JsonNode node : booksNode) {
                books.add(objectMapper.treeToValue(node, BookDTO.class));  // Converte o nó JSON para BookDTO
            }

            return books;  // Retorna a lista de livros por idioma

        } catch (HttpClientErrorException e) {
            // Em caso de erro de cliente (erro HTTP), lança uma exceção personalizada
            throw new ApiException("Erro ao buscar livros por idioma: " + e.getMessage(), e.getStatusCode().value());
        } catch (RestClientException e) {
            // Em caso de outro erro do cliente, lança uma exceção personalizada
            throw new ApiException("Erro ao fazer requisição para a API: " + e.getMessage(), 500); // Código 500 para erro genérico
        }
    }
}
