package br.com.alura.literalura.exception;

public class ApiException extends Exception {
    private int statusCode;  // Para armazenar o código de status HTTP

    // Construtor que aceita a mensagem de erro e o código de status HTTP
    public ApiException(String message, int statusCode) {
        super(message);  // Passa a mensagem para a classe pai (Exception)
        this.statusCode = statusCode;
    }

    // Getter para o código de status HTTP
    public int getStatusCode() {
        return statusCode;
    }
}

