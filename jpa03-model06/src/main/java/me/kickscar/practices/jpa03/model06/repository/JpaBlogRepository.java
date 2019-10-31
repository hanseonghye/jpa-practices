package me.kickscar.practices.jpa03.model06.repository;

import me.kickscar.practices.jpa03.model06.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlogRepository extends JpaRepository<User, Long> {
}
