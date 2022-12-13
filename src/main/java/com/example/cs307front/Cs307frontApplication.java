package com.example.cs307front;

import com.example.cs307front.utils.JdbcUtil;
import com.example.cs307front.utils.annotations.SqlSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@SqlSupport(
        DRIVER = "org.postgresql.Driver",
        USERNAME = "postgres",
        PASSWORD = "20030118",
        URL = "jdbc:postgresql://127.0.0.1:5432/sustc2?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatement=true"
)
public class Cs307frontApplication {
    public static void main(String[] args) {
        JdbcUtil.getConnection(Cs307frontApplication.class);
        SpringApplication.run(Cs307frontApplication.class, args);
    }

}
