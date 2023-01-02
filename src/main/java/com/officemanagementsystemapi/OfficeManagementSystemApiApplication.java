package com.officemanagementsystemapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class OfficeManagementSystemApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfficeManagementSystemApiApplication.class, args);
    }

}
