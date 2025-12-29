package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories;

import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE " +
            "LOWER(o.user.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(o.user.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(o.user.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(o.status) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Order> searchOrders(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Busca los pedidos de un usuario por su email y los ordena por fecha (el más reciente primero).
     */
    List<Order> findByUserEmailOrderByOrderDateDesc(String email);
}
