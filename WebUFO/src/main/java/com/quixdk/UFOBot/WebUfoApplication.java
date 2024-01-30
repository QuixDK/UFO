package com.quixdk.UFOBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@PropertySource("application.properties")
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
