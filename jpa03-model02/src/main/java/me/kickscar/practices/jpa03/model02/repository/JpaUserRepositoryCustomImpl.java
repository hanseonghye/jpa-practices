package me.kickscar.practices.jpa03.model02.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model02.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static me.kickscar.practices.jpa03.model02.domain.QUser.user;

public class JpaUserRepositoryCustomImpl extends QuerydslRepositorySupport implements JpaUserRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaUserRepositoryCustomImpl() {
        super(User.class);
    }

    @Override
    public void update(User paramUser) {
        queryFactory.update(user)
                .where(user.no.eq(paramUser.getNo()))
                .set(user.name, paramUser.getName())
                .set(user.email, paramUser.getEmail())
                .set(user.password, paramUser.getPassword())
                .set(user.gender, paramUser.getGender())
                .set(user.role, paramUser.getRole())
                .execute();
    }
}
