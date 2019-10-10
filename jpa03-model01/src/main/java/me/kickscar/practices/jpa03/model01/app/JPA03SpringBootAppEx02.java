package me.kickscar.practices.jpa03.model01.app;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.repository.JPQLRepository;
import me.kickscar.practices.jpa03.model01.repository.QueryDslRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class JPA03SpringBootAppEx02 {

    @Autowired
    QueryDslRepository repository;

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
        try(ConfigurableApplicationContext c = SpringApplication.run(JPA03SpringBootAppEx02.class, args)){}
    }
}
