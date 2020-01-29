package me.kickscar.practices.jpa03.model04.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import me.kickscar.practices.jpa03.model04.domain.Board;
import me.kickscar.practices.jpa03.model04.domain.Comment;
import me.kickscar.practices.jpa03.model04.domain.User;
import me.kickscar.practices.jpa03.model04.dto.BoardDto;

public class JpaBoardRepositoryTest2 {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBoardRepository boardRepository;

    @Autowired
    private JpaCommentRepository commentRepository;


    public void test01Save() {
        User user1 = new User();
        user1.setName("둘리");
        User user1Persisted = userRepository.save(user1);

        User user2 = new User();
        user2.setName("마이콜");
        User user2Persisted = userRepository.save(user2);

        Board board1 = new Board();
        board1.setTitle("제목1");
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("제목2");
        boardRepository.save(board2);

        commentRepository.save(1L, new Comment(user1Persisted, "댓글1"));
        commentRepository.save(2L, new Comment(user1Persisted, "댓글2"), new Comment(user2Persisted, "댓글3"));
    }


    public void test02SaveEagerProblem01() {
        User userPersisted = userRepository.findById(1L).get();
        commentRepository.save(1L, new Comment(userPersisted, "댓글1"));
    }


    public void test03BoardListLazyProblem() {
        Integer qryCount = 0;
        Long N = 2L;

        qryCount++;

        List<Board> boards = boardRepository.findAllByOrderByRegDateDesc();

        for(Board board : boards) {
            User user = board.getUser();

            if (!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(user)) {
                qryCount++;
            }
        }

        assertEquals(N+1, qryCount.longValue());
    }


    public void test04BoardListLazyProblemSolved() {
        Integer size = 2;
        Integer page = 0;
        List<BoardDto> boardDtos = null;
    }
}