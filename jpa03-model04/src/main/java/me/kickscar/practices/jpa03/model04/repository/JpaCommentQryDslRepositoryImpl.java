package me.kickscar.practices.jpa03.model04.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model04.domain.Board;
import me.kickscar.practices.jpa03.model04.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static me.kickscar.practices.jpa03.model04.domain.QComment.comment;

public class JpaCommentQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaCommentQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaCommentQryDslRepositoryImpl() {
        super(Comment.class);
    }

    @Override
    public void save(Long boardNo, Comment comment) {
        EntityManager em = getEntityManager();
        em.persist(comment);

        Board board = em.find(Board.class, boardNo);
        board.getComments().add(comment);

        em.persist(board);
    }
}
