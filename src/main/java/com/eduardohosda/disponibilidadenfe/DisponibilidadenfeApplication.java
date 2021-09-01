package com.eduardohosda.disponibilidadenfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DisponibilidadenfeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisponibilidadenfeApplication.class,args);
    }
}
