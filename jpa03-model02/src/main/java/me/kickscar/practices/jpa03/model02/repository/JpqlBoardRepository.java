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
        Query query = em.createQuery("delete from board b where b.no = ?1 and b.user.no = ?2");
        query.setParameter(1, boardNo);
        query.setParameter(2, userNo);

        return query.executeUpdate() == 1;
    }

    // 조회1(Fetch One)
    public Board find(Long no) { // 1차 캐시(영속컨텍스트)에서 찾고 없으면 Fetch One from DB
        return em.find(Board.class, no);
    }

    // 조회2(Fetch One)
    public Board find2(Long no) { // Unconditionally(무조건) Fetch One SQL from DB
        TypedQuery<Board> query = em.createQuery("select b from Board b where b.no = :no", Board.class);
        query.setParameter("no", no);
        return query.getSingleResult();
    }

    // 집합함수
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(b) from Board b", Long.class);
        return query.getSingleResult();
    }

    // Fetch Paging List(페이징 API 적용): 예제 데이터 수는 3개씩
    public List<Board> findAll(Integer page) {
        TypedQuery<Board> query = em.createQuery("select b from Board b order by b.regDate desc", Board.class);
        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    // LIKE 검색 Fetch Paging List(페이징 API 적용): 예제 데이터 수는 3개씩
    public List<Board> findAll(String keyword, Integer page) {
        TypedQuery<Board> query = em.createQuery("select b from Board b where b.title like :keywordContains or b.contents like :keywordContains order by b.regDate desc", Board.class);
        query.setParameter("keywordContains", "%" + keyword + "%");
        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }
}
