package me.kickscar.practices.jpa03.model11.repository;

import me.kickscar.practices.jpa03.model11.domain.CartItemId;
import me.kickscar.practices.jpa03.model11.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
