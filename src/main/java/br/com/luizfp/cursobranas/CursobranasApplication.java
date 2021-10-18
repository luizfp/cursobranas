package br.com.luizfp.cursobranas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CursobranasApplication {

    public static void main(String[] args) {
        SpringApplication.run(CursobranasApplication.class, args);
    }
}
