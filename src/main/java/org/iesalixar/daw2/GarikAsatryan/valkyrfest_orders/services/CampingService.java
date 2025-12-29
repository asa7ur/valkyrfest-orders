package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Camping;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.CampingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampingService {

    private final CampingRepository campingRepository;

    public List<Camping> getAllCampings() {
        return campingRepository.findAll();
    }

    public Optional<Camping> getCampingById(Long id) {
        return campingRepository.findById(id);
    }

    @Transactional
    public void saveCamping(Camping camping) {
        campingRepository.save(camping);
    }

    @Transactional
    public void deleteCamping(Long id) {
        campingRepository.deleteById(id);
    }

    public Page<Camping> getAllCampings(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return campingRepository.searchCampings(searchTerm, pageable);
        }
        return campingRepository.findAll(pageable);
    }
}