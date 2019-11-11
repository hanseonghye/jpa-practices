package me.kickscar.practices.jpa03.model12.repository;

import me.kickscar.practices.jpa03.model12.domain.Book;
import me.kickscar.practices.jpa03.model12.domain.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookRepository extends JpaRepository<Book, Long> {
}
