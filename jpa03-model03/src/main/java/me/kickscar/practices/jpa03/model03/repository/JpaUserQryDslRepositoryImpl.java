package me.kickscar.practices.jpa03.model03.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model03.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static me.kickscar.practices.jpa03.model03.domain.QUser.user;

public class JpaUserQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaUserQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaUserQryDslRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Boolean update(User argUser) {
        return queryFactory.update(user)
                .where(user.no.eq(argUser.getNo()))
                .set(user.name, argUser.getName())
                .set(user.email, argUser.getEmail())
                .set(user.password, argUser.getPassword())
                .set(user.gender, argUser.getGender())
                .set(user.role, argUser.getRole())
                .execute() == 1L;
    }
}
