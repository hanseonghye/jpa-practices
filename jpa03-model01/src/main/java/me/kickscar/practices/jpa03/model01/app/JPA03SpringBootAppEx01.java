package me.kickscar.practices.jpa03.model01.app;

import me.kickscar.practices.jpa03.model01.repository.JPQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.EntityManager;

@SpringBootApplication
@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model01.repository" } )
public class JPA03SpringBootAppEx01 {

    @Autowired
    JPQLRepository repository;

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {
                System.out.println( repository.getEntityManager() );
            }

            public void testInsert( EntityManager em ) {
            }

            public void testFind01( EntityManager em ) {
            }

            public void testIdentity( EntityManager em ) {
            }

            public void testFind02( EntityManager em ) {
            }
        };
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(JPA03SpringBootAppEx01.class, args)){}
    }
}
