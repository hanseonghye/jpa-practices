package me.kickscar.practices.jpa03.model02.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model02.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.List;

import static me.kickscar.practices.jpa03.model02.domain.QBoard.board;

@Repository
public class QueryDslBoardRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory queryFactory;

    public QueryDslBoardRepository() {
        super(Board.class);
    }

    // 저장(영속화)
    public void save(Board board){
        getEntityManager().persist(board);
    }

    // 조회1: Fetch One
    public Board find1(Long no) {
        // 1차 캐시(영속컨텍스트)에서 찾고 없으면 DB에서 가져오게 된다.
        return getEntityManager().find(Board.class, no);
    }

    // 조회2: Fetch One
    public Board find2(Long no) {
        // 무조건(Unconditionally) DB에서 가져온다.
        return (Board)queryFactory
                .from(board)
                .where(board.no.eq(no))
                .fetchOne();
    }

    // 조회3: Fetch List: Paging, 예제 데이터 수는 3개씩
    public List<Board> findAll(Integer page){
        return (List<Board>)queryFactory
                .from(board)
                .orderBy(board.regDate.desc())
                .offset((page - 1) * 3)
                .limit(3)
                .fetch();
    }

    // 조회4: Fetch List: Paging, LIKE 검색, 예제 데이터 수는 3개씩
    public List<Board> findAll(String keyword, Integer page){
        return (List<Board>)queryFactory
                .from(board)
                .where(board.title.contains(keyword).or(board.contents.contains(keyword)))
                .orderBy(board.regDate.desc())
                .offset((page - 1) * 3)
                .fetch();
    }

    // 수정


    // 삭제
    public Boolean remove(Long boardNo, Long userNo) {
        return queryFactory
                .delete(board)
                // 다음 2개의 where절은 완전 동일
                // .where(board.no.eq(boardNo).and(board.user.no.eq(userNo)))
                .where(board.no.eq(boardNo), board.user.no.eq(userNo))
                .execute() == 1;
    }

    // count
    public Long count() {
        return queryFactory
                .from(board)
                .fetchCount();
    }

}
