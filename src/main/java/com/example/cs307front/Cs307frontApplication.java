package com.example.cs307front;


import main.utils.JdbcUtil;
import main.utils.annotations.SqlSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@SqlSupport(URL = "jdbc:postgresql://127.0.0.1:5432/sustc2?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatement=true")
public class Cs307frontApplication {
    public static void main(String[] args) {
        JdbcUtil.getConnection(Cs307frontApplication.class);
        SpringApplication.run(Cs307frontApplication.class, args);
    }
}
