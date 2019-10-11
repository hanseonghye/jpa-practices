package me.kickscar.practices.jpa03.model01.app03;

//import org.springframework.boot.SpringApplication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
//@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model01.config", "me.kickscar.practices.jpa03.model01.repository" } )
public class Jpa03SpringBootRestApp03 {

    @RestController
    public class MyController {

        @GetMapping("/rest")
        public String rest() {
            return "Hello World";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run( Jpa03SpringBootRestApp03.class, args );
    }
}
