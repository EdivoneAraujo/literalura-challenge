package br.com.alura.literalura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private LocalDate birthYear;
    private LocalDate deathYear;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Book> books = new HashSet<>();

    public Author() {
    }

    public Author(String name, String birthYear, String deathYear) {
        this.name = name;
        try {
            this.birthYear = LocalDate.parse(birthYear);
            this.deathYear = LocalDate.parse(deathYear);
        } catch (DateTimeParseException ex) {
            this.birthYear = null;
            this.deathYear = null;
        }
    }

    @Override
    public String toString() {
        String booksStr = books.stream().map(Book::getTitle).collect(Collectors.joining(", "));

        return "Author{" +
                "name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", deathYear=" + deathYear +
                ", books=" + booksStr +
                '}';
    }

    public String getName() {
        return name;
    }
}