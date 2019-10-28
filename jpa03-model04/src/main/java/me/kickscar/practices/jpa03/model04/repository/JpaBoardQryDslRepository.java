package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.domain.Board;
import me.kickscar.practices.jpa03.model04.dto.BoardDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JpaBoardQryDslRepository {
    public BoardDto findById2(Long no);
    public Board findById3(Long no);
    public BoardDto findById4(Long no);

    public List<BoardDto> findAll3(Pageable pageable);
    public List<BoardDto> findAll3(String keyword, Pageable pageable);
    public Boolean update(Board board);
    public Boolean delete(Long boardNo, Long userNo);
}
