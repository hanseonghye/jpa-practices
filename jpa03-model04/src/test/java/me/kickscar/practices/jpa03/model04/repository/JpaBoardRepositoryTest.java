package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.config.JpaRepositoryTestConfig;
import me.kickscar.practices.jpa03.model04.domain.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaRepositoryTestConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaBoardRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBoardRepository boardRepository;

    @Autowired
    private JpaCommentRepository commentRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save(){
        User user1 = new User();
        user1.setName("둘리");
        user1.setPassword("1234");
        user1.setEmail("dooly@kickscar.me");
        user1.setGender(GenderType.MALE);
        user1.setRole(RoleType.USER);
        userRepository.save(user1);

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

        Comment comment1 = new Comment();
        comment1.setContents("댓글1");
        commentRepository.save(1L, comment1);

        Comment comment2 = new Comment();
        comment2.setContents("댓글2");
        commentRepository.save(1L, comment2);

        Comment comment3 = new Comment();
        comment3.setContents("댓글3");
        commentRepository.save(2L, comment3);

        Comment comment4 = new Comment();
        comment4.setContents("댓글4");
        commentRepository.save(2L, comment4);

        //============================================

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        userRepository.save(user2);

        Board board3 = new Board();
        board3.setTitle("제목3");
        board3.setContents("내용3");
        board3.setUser(user2);
        boardRepository.save(board3);

        Board board4 = new Board();
        board4.setTitle("제목4");
        board4.setContents("내용4");
        board4.setUser(user2);
        boardRepository.save(board4);

        Comment comment5 = new Comment();
        comment5.setContents("댓글5");
        commentRepository.save(3L, comment5);

        Comment comment6 = new Comment();
        comment6.setContents("댓글6");
        commentRepository.save(3L, comment6);

        Comment comment7 = new Comment();
        comment7.setContents("댓글7");
        commentRepository.save(4L, comment7);

        Comment comment8 = new Comment();
        comment8.setContents("댓글8");
        commentRepository.save(4L, comment8);

        assertEquals(8L, commentRepository.count());
    }
}