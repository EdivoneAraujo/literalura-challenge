package br.com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
        String title,
        Set<AuthorDTO> authors,
        Set<String> languages,
        @JsonAlias("download_count") Integer downloadCount) {
}
