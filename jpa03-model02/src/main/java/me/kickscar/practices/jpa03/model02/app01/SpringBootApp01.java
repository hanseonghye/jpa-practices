package me.kickscar.practices.jpa03.model02.app01;

import me.kickscar.practices.jpa03.model02.app01.repository.JpqlBoardRepository;
import me.kickscar.practices.jpa03.model02.app01.repository.JpqlUserRepository;
import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootApplication
@ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model02.app01.config", "me.kickscar.practices.jpa03.model02.app01.repository" } )
public class SpringBootApp01 {

    @Service
    @Transactional
    public class TestService {

        @Autowired
        JpqlUserRepository userRepository;

        @Autowired
        JpqlBoardRepository boardRepository;

        public void insertUser() {
            User user1 = new User();
            user1.setName( "둘리" );
            user1.setPassword( "1234" );
            user1.setEmail( "dooly@kickscar.me" );
            user1.setGender(GenderType.MALE);
            user1.setRole(RoleType.USER);
            userRepository.save( user1 );

            User user2 = new User();
            user2.setName( "마이콜" );
            user2.setPassword( "1234" );
            user2.setEmail( "michol@kickscar.me" );
            user2.setGender(GenderType.MALE);
            user2.setRole(RoleType.USER);
            userRepository.save( user2 );
        }

        public void insertBoard() {
            User user1 = userRepository.find(1L);
            Board board1 = new Board();
            board1.setTitle("제목1");
            board1.setContents("내용1");
            board1.setUser(user1);
            boardRepository.save(board1);

            User user2 = userRepository.find(2L);
            Board board2 = new Board();
            board2.setTitle("제목2");
            board2.setContents("내용2");
            board2.setUser(user2);
            boardRepository.save(board2);
        }

        public void fetchBoard(){
            Board board = boardRepository.find(1L);
//            System.out.println(board);
        }
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            private TestService service;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                service.insertUser();
                service.insertBoard();
                service.fetchBoard();
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
