package me.kickscar.practices.jpa.ch01.app;

import me.kickscar.practices.jpa.ch01.domain.Member;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

@SpringBootApplication
// 엔티티 클래스 자동스캐닝 베이스 패키지 지정
@EntityScan( basePackages = { "me.kickscar.practices.jpa.ch01.domain" } )
public class Ex01SpringBootApp {

    //엔티티매니저팩토리 주입
    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {

                /* 아무것도 안하고 스키마만 생성 : 로그에 DDL 확인해 볼 것 */

                entityManagerFactory.close();
            }
        };
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(Ex01SpringBootApp.class, args)){
        }
    }
}
