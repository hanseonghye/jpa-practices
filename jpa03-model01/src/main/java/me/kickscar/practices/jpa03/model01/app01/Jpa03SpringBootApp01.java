package me.kickscar.practices.jpa03.model01.app01;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.repository.Model01JpqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model01.config", "me.kickscar.practices.jpa03.model01.repository" } )
public class Jpa03SpringBootApp01 {

    @Autowired
    Model01JpqlRepository repository;

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Override
            public void run(ApplicationArguments args) throws Exception {
                testInsert();
                testFindAll();

                testRemove();
                testFindAll();
            }

            public void testInsert() {
                Guestbook gb1 = new Guestbook();
                gb1.setName("둘리");
                gb1.setPassword("1234");
                gb1.setContents("호이~");
                repository.save(gb1);

                Guestbook gb2 = new Guestbook();
                gb2.setName("마이콜");
                gb2.setPassword("1234");
                gb2.setContents("라면은 구공탄에~~~\n 후르르 짭짭 맛있는 라면~~");
                repository.save(gb2);
            }

            public void testFindAll() {
                List<Guestbook> list = repository.findAll();
                for(Guestbook gb : list){
                    System.out.println(gb);
                }
            }

            public void testRemove() {
                Guestbook gb = new Guestbook();
                gb.setNo(1L);
                gb.setPassword("1234");

                repository.remove(gb);
            }
        };
    }

    public static void main(String[] args) {
        /*
            prevent spring-boot autoconfiguration for spring-web :
            jpa03 모듈에는 Rest Application이 있기 떄문에 org.springframework.boot:spring-boot-starter-web 에 의존성이 있음
            콘솔 Command Line App 에서는 Web 자동설정이 문제가 발생할 수 있기 때문에 막음.
        */
        try( ConfigurableApplicationContext c =
                     new SpringApplicationBuilder( Jpa03SpringBootApp01.class )
                             .web( WebApplicationType.NONE )
                             .run( args ) ){

        }
    }
}
