package br.com.alura.literalura.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;

@RestController
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    private ConnectionUtil connectionUtil;

    @GetMapping("/create-table")
    public String createTable() {
        Connection conn = connectionUtil.connect_to_db("seu_banco", "seu_usuario", "sua_senha");
        connectionUtil.createTable(conn, "recovery");
        return "Table created!";
    }

    @PostMapping("/insert")
    public String insertRow(@RequestParam int socialSecurity, @RequestParam double balance) {
        Connection conn = connectionUtil.connect_to_db("seu_banco", "seu_usuario", "sua_senha");
        connectionUtil.insert_row(conn, socialSecurity, balance);
        return "Row inserted!";
    }
}
