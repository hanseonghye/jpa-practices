package me.kickscar.practices.jpa03.model02.app01.repository;

import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public class JpqlBoardRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Board board){
        em.persist(board);
    }

    public Board find(Long no){
        Board board = em.find(Board.class, no);
        board.getUser().getName();
        return board;
    }

    public List<Board> findAll(){
        TypedQuery query = em.createQuery("select b from Board b order by b.regDate desc", Board.class);
        List<Board> list = query.getResultList();
        return list;
    }

//
//    public Boolean remove(Long no){
//
//        //
//        // :의 용도는  setParameter
//        //
//        TypedQuery query = em.createQuery("select b from Board b where b.no= :no", Board.class);
//        query.setParameter("no", no);
//
//        // 결과가 잘못되면 error handling을 해야하는 번잡함이 있다. 따라서 다음과 같은 코드가 효율적임.
//        // Board result = query.getSingleResult();
//        List<Board> list = query.getResultList();
//        if(list.size() != 1) {
//            return false;
//        }
//
//        em.remove(list.get(0));
//
//        return true;
//    }



}
