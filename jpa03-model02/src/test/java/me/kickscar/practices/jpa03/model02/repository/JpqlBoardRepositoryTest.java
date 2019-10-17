package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.conf.JpqlRepositoryTestConfig;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
@Transactional
public class JpqlBoardRepositoryTest {

    @Autowired
    private JpqlUserRepository userRepository;

    @Autowired
    private JpqlBoardRepository boardRepository;

    @Test
    @Rollback(false)
    public void test01Save(){
        User user = new User();
        user.setName("둘리");
        user.setPassword("1234");
        user.setEmail("dooly@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
        userRepository.save(user);

        Board board1 = new Board();
        board1.setTitle("제목1");
        board1.setContents("내용1");
        board1.setUser(user);
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("제목2");
        board2.setContents("내용2");
        board2.setUser(user);
        boardRepository.save(board2);

        Board board3 = new Board();
        board3.setTitle("제목3");
        board3.setContents("내용3");
        board3.setUser(user);
        boardRepository.save(board3);

        Board board4 = new Board();
        board4.setTitle("제목4");
        board4.setContents("내용4");
        board4.setUser(user);
        boardRepository.save(board4);

        Board board5 = new Board();
        board5.setTitle("제목5");
        board5.setContents("내용5");
        board5.setUser(user);
        boardRepository.save(board5);

        assertEquals(5L, boardRepository.count().longValue());
    }

    @Test
    public void test02Find1(){
        Board board = boardRepository.find1(1L);
        assertEquals(1L, board.getNo().longValue());
    }

    @Test
    public void test03Find2(){
        Board board = boardRepository.find2(1L);
        assertEquals(1L, board.getNo().longValue());
    }

    @Test
    public void test04Find1IsFetchEager(){
        Long no = 2L;
        Board board = boardRepository.find1(no);

        // Eager Fetch는 Proxy 객체 타입을 리턴하지 않는다.
        // Lazy Fetch는 Proxy 객체를 리턴한다.(실제 User 객체가 아니다)
        assertEquals(User.class, board.getUser().getClass());
    }

    @Test
    public void test05FindAllPaging(){
        Integer page = 1;
        List<Board> list1 = boardRepository.findAll(page++);
        assertEquals(3, list1.size());

        List<Board> list2 = boardRepository.findAll(page++);
        assertEquals(2, list2.size());

        List<Board> list3 = boardRepository.findAll(page++);
        assertEquals(0, list3.size());
    }

    @Test
    public void test06FindAllLikeSearchAndPaging(){
        Integer page = 1;
        String keyword = "내용";

        List<Board> list1 = boardRepository.findAll(keyword, page++);
        assertEquals(3, list1.size());

        List<Board> list2 = boardRepository.findAll(keyword, page++);
        assertEquals(2, list2.size());

        List<Board> list3 = boardRepository.findAll(keyword, page++);
        assertEquals(0, list3.size());
    }
}
