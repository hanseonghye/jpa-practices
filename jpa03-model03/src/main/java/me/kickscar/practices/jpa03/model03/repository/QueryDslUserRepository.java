package me.kickscar.practices.jpa03.model03.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model03.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static me.kickscar.practices.jpa03.model03.domain.QUser.user;

@Repository
public class QueryDslUserRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory queryFactory;

    public QueryDslUserRepository() {
        super(User.class);
    }

    // 저장(영속화)
    public void save(User user){
        getEntityManager().persist(user);
    }

    // count
    public Long count() {
        return queryFactory
                .from(user)
                .fetchCount();
    }
}
