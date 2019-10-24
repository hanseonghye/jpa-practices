package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.Order;
import me.kickscar.practices.jpa03.model03.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, Long>, JpaUserQryDslRepository {
    public User findByEmailAndPassword(String email, String password);
}
