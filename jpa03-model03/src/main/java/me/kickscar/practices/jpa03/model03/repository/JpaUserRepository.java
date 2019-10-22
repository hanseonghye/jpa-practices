package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.GenderType;
import me.kickscar.practices.jpa03.model03.domain.RoleType;
import me.kickscar.practices.jpa03.model03.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUserRepository extends JpaRepository<User, Long>, JpaUserQryDslRepository {
    public User findByEmailAndPassword(String email, String password);

    @Modifying
    @Query("update User u set u.name=:name, u.email=:email, u.password=:password, u.gender=:gender, u.role=:role where u.no=:no")
    public void update(
            @Param("no") Long no,
            @Param("name") String name,
            @Param("email") String email,
            @Param("password") String password,
            @Param("gender") GenderType gender,
            @Param("role") RoleType role);
}
