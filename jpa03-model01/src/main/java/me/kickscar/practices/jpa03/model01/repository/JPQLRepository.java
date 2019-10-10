package me.kickscar.practices.jpa03.model01.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JPQLRepository {
    @PersistenceContext                         //factory가 인식할 수 있게끔....
    private EntityManager em;

    public EntityManager getEntityManager(){
        return em;
    }

}
