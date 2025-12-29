package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories;

import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE " +
            "LOWER(t.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.documentType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.documentNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.ticketType.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.status) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Ticket> searchTickets(@Param("searchTerm") String searchTerm, Pageable pageable);
}
