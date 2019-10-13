package me.kickscar.practices.jpa03.model01.app01;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import me.kickscar.practices.jpa03.model01.app01.service.GuestbookService;

@SpringBootApplication
@ComponentScan( basePackages = {  // 하위 패키지에 Config, Service, Repository가 있기 때문에 자동스캔 가능(생략가능)
        "me.kickscar.practices.jpa03.model01.app01.config",
        "me.kickscar.practices.jpa03.model01.app01.service",
        "me.kickscar.practices.jpa03.model01.app01.repository" } )
public class SpringBootApp01 {

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            private GuestbookService guestbookService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                testAddMesssages();
                testMessageList();
                testDeleteMessage();
                testMessageList();
            }

            public void testAddMesssages() {
                Guestbook gb1 = new Guestbook();
                gb1.setName("둘리");
                gb1.setPassword("1234");
                gb1.setContents("안녕1");
                guestbookService.addMessage(gb1);

                Guestbook gb2 = new Guestbook();
                gb2.setName("마이콜");
                gb2.setPassword("1234");
                gb2.setContents("안녕2");
                guestbookService.addMessage(gb2);
            }

            public void testMessageList() {
                for(Guestbook gb : guestbookService.getMessageList()){
                    System.out.println(gb);
                }
            }

            public void testDeleteMessage() {
                Guestbook gb = new Guestbook();
                gb.setNo(1L);
                gb.setPassword("1234");
                guestbookService.deleteMessage(gb);
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
                     new SpringApplicationBuilder( SpringBootApp01.class )
                             .web( WebApplicationType.NONE )
                             .run( args ) ){

        }
    }
}
