package me.kickscar.practices.jpa03.model12.repository;

import me.kickscar.practices.jpa03.model12.domain.CartItem;
import me.kickscar.practices.jpa03.model12.domain.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
