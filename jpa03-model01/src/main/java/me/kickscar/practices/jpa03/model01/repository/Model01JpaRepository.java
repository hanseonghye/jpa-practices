package me.kickscar.practices.jpa03.model01.repository;

import java.util.List;

import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Model01JpaRepository extends JpaRepository<Guestbook, Long> {
	List<Guestbook> findAllByOrderByRegDateDesc();
	List<Guestbook> findAllByOrderByNameDesc(Pageable pageable);
	List<Guestbook> findByName(String name, Sort sort);
}
