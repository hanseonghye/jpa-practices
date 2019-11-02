package me.kickscar.practices.jpa03.model08.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model08.domain.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model08.domain.QBlog.blog;


public class JpaBlogQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaBlogQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaBlogQryDslRepositoryImpl() {
        super(Blog.class);
    }

    @Override
    public List<Blog> findAll2() {
        return queryFactory
                .select(blog)
                .from(blog)
                .innerJoin(blog.user)
                .fetchJoin()
                .fetch();
    }


}
