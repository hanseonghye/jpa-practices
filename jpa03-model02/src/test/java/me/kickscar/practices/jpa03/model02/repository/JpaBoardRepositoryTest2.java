package me.kickscar.practices.jpa03.model02.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.domain.User;

public class JpaBoardRepositoryTest2 {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBoardRepository boardRepository;
    

    public void test01Save() {
        User user1 = new User();
        user1.setName("둘리");
        
        User user1Persisted = userRepository.save(user1);

        Board board1 = new Board();
        board1.setTitle("제목1");
        boardRepository.save(board1);

        Board board1 = new Board();
        board1.setTitle("제목1");
        boardRepository.save(board1);
        
    }
}