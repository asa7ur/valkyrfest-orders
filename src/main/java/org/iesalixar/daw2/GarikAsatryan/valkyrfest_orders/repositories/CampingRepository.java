package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories;

import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Camping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampingRepository extends JpaRepository<Camping, Long> {
    @Query("SELECT c FROM Camping c WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.documentType) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.documentNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.campingType.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.status) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Camping> searchCampings(@Param("searchTerm") String searchTerm, Pageable pageable);
}
