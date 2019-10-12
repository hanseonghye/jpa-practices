package me.kickscar.practices.jpa03.model01.repository;

import java.util.List;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Model01JpaRepository extends JpaRepository<Guestbook, Long> {
	List<Guestbook> findAllByOrderByRegDateDesc();
	List<Guestbook> findAllByOrderByRegDateDesc(Pageable pageable);

	List<Guestbook> findAllByContents(String contents, Sort sort);

	int deleteByNoAndPassword(Long no, String passwd);
}
