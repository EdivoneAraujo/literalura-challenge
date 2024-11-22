package br.com.alura.literalura.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/create-table")
    public String createTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS recovery (" +
                    "id SERIAL PRIMARY KEY NOT NULL, " +
                    "balance NUMERIC(10, 2) DEFAULT 0, " +
                    "social_security INT4 NOT NULL UNIQUE);";
            jdbcTemplate.execute(sql); // Usando JdbcTemplate para executar o SQL
            return "Table created!";
        } catch (DataAccessException e) {
            return "Error creating table: " + e.getMessage();
        }
    }

    @PostMapping("/insert")
    public String insertRow(@RequestParam int socialSecurity, @RequestParam double balance) {
        try {
            String sql = "INSERT INTO recovery(balance, social_security) VALUES (?, ?)";
            jdbcTemplate.update(sql, balance, socialSecurity); // Usando JdbcTemplate para inserir os dados
            return "Row inserted!";
        } catch (DataAccessException e) {
            return "Error inserting row: " + e.getMessage();
        }
    }
}
