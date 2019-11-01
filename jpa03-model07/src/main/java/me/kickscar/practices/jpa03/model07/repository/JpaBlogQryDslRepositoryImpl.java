package me.kickscar.practices.jpa03.model07.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model07.domain.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class JpaBlogQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaBlogQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaBlogQryDslRepositoryImpl() {
        super(Blog.class);
    }
}
