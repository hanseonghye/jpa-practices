package me.kickscar.practices.jpa03.model02.app01.repository;

import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JpqlUserRepository {

    @PersistenceContext
    private EntityManager em;

    public User find(Long no) {
        return em.find(User.class, no);
    }

    public void save(User user){
        em.persist(user);
    }


}
