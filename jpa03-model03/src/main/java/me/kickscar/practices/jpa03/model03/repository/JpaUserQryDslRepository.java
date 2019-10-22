package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.User;

public interface JpaUserQryDslRepository {
    public Boolean update(User user);
}
