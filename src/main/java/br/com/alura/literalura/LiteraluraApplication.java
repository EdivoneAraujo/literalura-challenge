package br.com.alura.literalura;

import br.com.alura.literalura.dto.BookDTO;
import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.repository.BookRepository;
import br.com.alura.literalura.service.GutendexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
    @Autowired
    private GutendexService gutendexService;

    @Autowired
    private BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 0) {
            menu();
            option = scanner.nextInt();
            scanner.nextLine();  // Limpar o buffer

            switch (option) {
                case 1:
                    findBookByTitle(scanner);
                    break;

                case 2:
                    listBooks();
                    break;

                case 3:
                    listAuthors();
                    break;

                case 4:
                    findAuthorByName(scanner);
                    break;

                case 5:
                    listAuthorsAliveByYear(scanner);
                    break;

                case 6:
                    listBooksByLanguage(scanner);
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }

        scanner.close();
    }

    // Exibe o menu de opções
    public void menu() {
        System.out.println("1 - Buscar livro pelo título");
        System.out.println("2 - Listar livros salvos");
        System.out.println("3 - Listar autores salvos");
        System.out.println("4 - Buscar autor pelo nome");
        System.out.println("5 - Listar autores vivos em determinado ano");
        System.out.println("6 - Listar livros por idioma");
        System.out.println("0 - Sair");
        System.out.println("Escolha uma opção:");
    }

    // Método para buscar livros pelo título na API Gutendex
    private void findBookByTitle(Scanner scanner) {
    System.out.println("Digite o título ou autor do livro:");
    String query = scanner.nextLine();

    try {
        // Buscando livros sem especificar idioma
        List<BookDTO> books = gutendexService.searchBooks(query, 1, 10);  // Paginação (Página 1, 10 resultados por página)

        if (books.isEmpty()) {
            System.out.println("Nenhum livro encontrado");
        } else {
            for (int i = 0; i < books.size(); i++) {
                BookDTO book = books.get(i);
                System.out.println(i + " - Título: " + book.title() +
                        ", Autor(es): " + book.authors() +
                        ", Idioma(s): " + book.languages());
            }

            System.out.println("Digite o número do livro que deseja salvar:");
            int index = scanner.nextInt();
            scanner.nextLine();

            // Salvar o livro selecionado
            Book book = new Book(books.get(index));
            bookRepository.save(book);
            System.out.println("Livro salvo com sucesso!");
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}

    // Método para listar todos os livros salvos no banco de dados
    private void listBooks() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            System.out.println("Nenhum livro salvo");
        } else {
            System.out.println("Lista de livros salvos:");
            for (Book book : books) {
                System.out.println("- " + book.getTitle());
            }
        }
    }

    // Método para listar todos os autores salvos no banco de dados
    private void listAuthors() {
        List<Author> authors = bookRepository.findAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor salvo");
        } else {
            System.out.println("Lista de autores salvos:");
            for (Author author : authors) {
                System.out.println("- " + author.getName());
            }
        }
    }

    // Método para buscar autores pelo nome
    private void findAuthorByName(Scanner scanner) {
        System.out.println("Digite o nome do autor:");
        String name = scanner.nextLine();

        List<Author> authors = bookRepository.findByAuthorsName(name);

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor encontrado com esse nome");
        } else {
            System.out.println("Autores encontrados:");
            for (Author author : authors) {
                System.out.println("- " + author.getName());
            }
        }
    }

    // Método para listar autores vivos em um determinado ano
    private void listAuthorsAliveByYear(Scanner scanner) {
        System.out.println("Digite o ano:");
        int year = scanner.nextInt();
        scanner.nextLine();

        List<Author> authors = bookRepository.findByAuthorsBirthYearLessThanEqual(year);

        if (authors.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo em " + year);
        } else {
            System.out.println("Autores vivos em " + year + ":");
            for (Author author : authors) {
                System.out.println("- " + author.getName());
            }
        }
    }

    // Método para listar livros por idioma
    private void listBooksByLanguage(Scanner scanner) {
        System.out.println("Digite o idioma (ex: 'pt' para Português, 'en' para Inglês):");
        String language = scanner.nextLine();

        try {
            List<BookDTO> books = gutendexService.searchBooksByLanguage(language, 1, 10);

            if (books.isEmpty()) {
                System.out.println("Nenhum livro encontrado para o idioma " + language);
            } else {
                for (int i = 0; i < books.size(); i++) {
                    BookDTO book = books.get(i);
                    System.out.println(i + " - Título: " + book.title() +
                            ", Autor(es): " + book.authors() +
                            ", Idioma: " + book.languages());
                }

                System.out.println("Digite o número do livro que deseja salvar:");
                int index = scanner.nextInt();
                scanner.nextLine();  // Limpar o buffer

                Book book = new Book(books.get(index));
                bookRepository.save(book);
                System.out.println("Livro salvo com sucesso!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}