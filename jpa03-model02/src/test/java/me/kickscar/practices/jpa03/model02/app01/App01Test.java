package me.kickscar.practices.jpa03.model02.app01;

import me.kickscar.practices.jpa03.model02.app01.config.JpaConfig;
import me.kickscar.practices.jpa03.model02.app01.repository.JpqlBoardRepository;
import me.kickscar.practices.jpa03.model02.app01.repository.JpqlUserRepository;
import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App01Test.Config.class})
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
public class App01Test {

    @Autowired
    private JpqlUserRepository userRepository;

    @Autowired
    private JpqlBoardRepository boardRepository;

    @Test
    @Rollback(false)
    public void test01InsertUser(){
        User user1 = new User();
        user1.setName( "둘리" );
        user1.setPassword( "1234" );
        user1.setEmail( "dooly@kickscar.me" );
        user1.setGender(GenderType.MALE);
        user1.setRole(RoleType.USER);
        userRepository.save(user1);

        assertEquals( 1L,  userRepository.count().longValue() );
    }

    @Test
    @Rollback(false)
    public void test02InsertBoard(){
        assertEquals( 1L,  userRepository.count().longValue() );

        User user1 = userRepository.find(1L);

        Board board1 = new Board();
        board1.setTitle("제목1");
        board1.setContents("내용1");
        board1.setUser(user1);
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("제목2");
        board2.setContents("내용2");
        board2.setUser(user1);
        boardRepository.save(board2);

        assertEquals( 2L, boardRepository.count().longValue() );
    }

    @Test
    public void test03FetchBoard(){
        Board board = boardRepository.find(1L);
        assertEquals(1L, board.getNo().longValue() );
    }

    @Test
    public void test04EagerFetchBoard(){
        Board board = boardRepository.find(2L);
        assertEquals( User.class, board.getUser().getClass() );
    }

    @Configuration
    @ComponentScan( basePackages = { "me.kickscar.practices.jpa03.model02.app01.repository" })
    @Import(JpaConfig.class)
    public static class Config {
    }
}
