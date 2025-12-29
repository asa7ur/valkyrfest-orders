package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Ticket;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    @Transactional
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public Page<Ticket> getAllTickets(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return ticketRepository.searchTickets(searchTerm, pageable);
        }
        return ticketRepository.findAll(pageable);
    }
}