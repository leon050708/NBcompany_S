package org.example.nbcompany;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.nbcompany.dao")
public class NbCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbCompanyApplication.class, args);
    }

}
