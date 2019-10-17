package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpqlBoardRepository {

    @Autowired
    private EntityManager em;

    // 저장(영속화)
    public void save(Board board){
        em.persist(board);
    }

    // 삭제
    public Boolean remove(Long boardNo, Long userNo) {
        String qlString = "delete from board b where b.no = ?1 and b.user.no = ?2";
        Query query = em.createQuery(qlString);

        query.setParameter(1, boardNo);
        query.setParameter(2, userNo);

        return query.executeUpdate() == 1;
    }

    // 조회1(Fetch One)
    public Board find1(Long no) { // 1차 캐시(영속컨텍스트)에서 찾고 없으면 DB에서 가져온다.
        return em.find(Board.class, no);
    }

    // 조회2(Fetch One)
    public Board find2(Long no) { // Unconditionally(무조건) 디비에서 가져온다.
        String qlString = "select b from Board b where b.no = :no";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        query.setParameter("no", no);

        return query.getSingleResult();
    }

    // count
    public Long count() {
        String qlString = "select count(b) from Board b";
        TypedQuery<Long> query = em.createQuery(qlString, Long.class);

        return query.getSingleResult();
    }

    // Fetch List(페이징): 데이터 수는 3개씩
    public List<Board> findAll(Integer page) {
        String qlString = "select b from Board b order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    // Fetch List (LIKE 검색, 페이징): 데이터 수는 3개씩
    public List<Board> findAll(String keyword, Integer page) {
        String qlString = "select b from Board b where b.title like :keywordContains or b.contents like :keywordContains order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        query.setParameter("keywordContains", "%" + keyword + "%");
        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }
}
