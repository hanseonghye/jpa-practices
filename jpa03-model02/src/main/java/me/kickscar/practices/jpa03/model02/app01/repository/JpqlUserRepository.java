package me.kickscar.practices.jpa03.model02.app01.repository;

import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class JpqlUserRepository {

    @PersistenceContext
    private EntityManager em;

    //
    public User find(Long no) {
        return em.find(User.class, no);
    }

    // 영속화
    public void save(User user){
        em.persist(user);
    }

    // JPQL 집합함수 사용법
    public Long count() {
        TypedQuery<Long> query = em.createQuery("select count(u) from User u", Long.class);
        return query.getSingleResult();
    }
}
