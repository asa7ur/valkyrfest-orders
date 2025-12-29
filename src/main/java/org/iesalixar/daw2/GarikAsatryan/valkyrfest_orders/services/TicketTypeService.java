package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.TicketType;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.TicketTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    public Optional<TicketType> getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id);
    }

    @Transactional
    public TicketType saveTicketType(TicketType ticketType) {
        return ticketTypeRepository.save(ticketType);
    }

    @Transactional
    public void deleteTicketType(Long id) {
        ticketTypeRepository.deleteById(id);
    }
}