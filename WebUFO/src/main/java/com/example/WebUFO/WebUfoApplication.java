package com.example.WebUFO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class WebUfoApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(WebUfoApplication.class, args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
