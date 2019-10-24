package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserDto;

public interface JpaUserQryDslRepository {
    public UserDto findById2(Long no);
    public Boolean update(User user);
}
