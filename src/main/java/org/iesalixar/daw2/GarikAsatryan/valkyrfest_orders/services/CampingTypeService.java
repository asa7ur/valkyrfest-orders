package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.CampingType;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.CampingTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampingTypeService {

    private final CampingTypeRepository campingTypeRepository;

    public List<CampingType> getAllCampingTypes() {
        return campingTypeRepository.findAll();
    }

    public Optional<CampingType> getCampingTypeById(Long id) {
        return campingTypeRepository.findById(id);
    }

    @Transactional
    public CampingType saveCampingType(CampingType campingType) {
        return campingTypeRepository.save(campingType);
    }

    @Transactional
    public void deleteCampingType(Long id) {
        campingTypeRepository.deleteById(id);
    }
}