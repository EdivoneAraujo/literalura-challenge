package br.com.alura.literalura.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class ConnectionUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate; // Usaremos JdbcTemplate para facilitar a interação com o banco de dados

    // Método para criar a tabela 'recovery' caso ela não exista
    public void createTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS recovery (" +
                    "id SERIAL PRIMARY KEY NOT NULL, " +
                    "balance NUMERIC(10, 2) DEFAULT 0, " +
                    "social_security INT4 NOT NULL UNIQUE);";
            jdbcTemplate.execute(sql); // Usando JdbcTemplate para executar o SQL
            System.out.println("Table 'recovery' created or already exists.");
        } catch (DataAccessException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Método para inserir uma linha na tabela 'recovery'
    public void insertRow(int socialSecurity, double balance) {
        try {
            String sql = "INSERT INTO recovery(balance, social_security) VALUES (?, ?)";
            jdbcTemplate.update(sql, balance, socialSecurity); // Usando JdbcTemplate para inserir os dados
            System.out.println("Row inserted into 'recovery' table.");
        } catch (DataAccessException e) {
            System.out.println("Error inserting row: " + e.getMessage());
        }
    }

    // Método para exibir os saldos presentes na tabela 'recovery'
    public void displayBalance() {
        try {
            String sql = "SELECT * FROM recovery";
            jdbcTemplate.query(sql, (ResultSet rs) -> {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    double balance = rs.getDouble("balance");
                    int socialSecurity = rs.getInt("social_security");
                    System.out.printf("%d - Balance: %.2f - SS: %d%n", id, balance, socialSecurity);
                }
            });
        } catch (DataAccessException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
    }

    // Método para excluir uma linha da tabela 'recovery' com base no ID
    public void deleteRow(int id) {
        try {
            String sql = "DELETE FROM recovery WHERE id = ?";
            jdbcTemplate.update(sql, id); // Usando JdbcTemplate para excluir o dado
            System.out.println("Row with ID " + id + " deleted from 'recovery' table.");
        } catch (DataAccessException e) {
            System.out.println("Error deleting row: " + e.getMessage());
        }
    }

}