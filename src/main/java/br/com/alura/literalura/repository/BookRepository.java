package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByLanguage(String language);

    @Query("SELECT DISTINCT a FROM Book b JOIN b.authors a WHERE YEAR(a.birthYear) <= :year")
    List<Author> findByAuthorsBirthYearLessThanEqual(int year);

    @Query("SELECT b.authors FROM Book b")
    List<Author> findAllAuthors();

    @Query("SELECT a FROM Book b JOIN b.authors a WHERE lower(a.name) LIKE lower(concat('%', :name, '%'))")
    List<Author> findByAuthorsName(String name);

}