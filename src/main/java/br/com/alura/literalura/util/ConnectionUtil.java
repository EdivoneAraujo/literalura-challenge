package br.com.alura.literalura.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
public class ConnectionUtil {
    public Connection connect_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    public void createTable(Connection conn, String recovery) {
        try (Statement statement = conn.createStatement()) {
            String query = "CREATE TABLE " + recovery + " (" +
                    "id SERIAL PRIMARY KEY NOT NULL, " +
                    "balance NUMERIC(10, 2) DEFAULT 0, " +
                    "social_security INT4 NOT NULL UNIQUE);";
            statement.executeUpdate(query);
            System.out.println("Table created: " + recovery);
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void insert_row(Connection conn, int socials, double transfer) {
        try (Statement statement = conn.createStatement()) {
            String query = "INSERT INTO recovery(balance, social_security) VALUES (" + transfer + "," + socials + ");";
            statement.executeUpdate(query);
            System.out.println("Row inserted");
        } catch (Exception e) {
            System.out.println("Error inserting row: " + e.getMessage());
        }
    }

    public void balance(Connection conn, String recovery) {
        try (Statement statement = conn.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM " + recovery + ";")) {
            while (result.next()) {
                int id = result.getInt("id");
                String privatebalances = result.getString("balance");
                String socialsecurity = result.getString("social_security");

                System.out.printf("%d - Balance: %s - SS: %s%n", id, privatebalances, socialsecurity);
            }
        } catch (Exception e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
    }

    public void delete(Connection conn, int idm) {
        try (Statement statement = conn.createStatement()) {
            String query = "DELETE FROM recovery WHERE id=" + idm + ";";
            statement.executeUpdate(query);
            System.out.println("Account with ID " + idm + " deleted.");
        } catch (Exception e) {
            System.out.println("Error deleting row: " + e.getMessage());
        }
    }
}

