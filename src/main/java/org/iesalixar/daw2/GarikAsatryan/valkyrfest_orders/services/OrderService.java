package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.CampingOrderDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.OrderRequestDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.TicketOrderDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.*;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions.AppException;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.CampingTypeRepository;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.OrderRepository;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.TicketTypeRepository;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final CampingTypeRepository campingTypeRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Page<Order> getAllOrders(String searchTerm, Pageable pageable) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            return orderRepository.searchOrders(searchTerm, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByUserEmailOrderByOrderDateDesc(email);
    }

    public BigDecimal calculateTotal(OrderRequestDTO request) {
        BigDecimal total = BigDecimal.ZERO;

        if (request.getTickets() != null) {
            for (var t : request.getTickets()) {
                total = total.add(ticketTypeRepository.findById(t.getTicketTypeId())
                        .map(TicketType::getPrice).orElse(BigDecimal.ZERO));
            }
        }

        if (request.getCampings() != null) {
            for (var c : request.getCampings()) {
                total = total.add(campingTypeRepository.findById(c.getCampingTypeId())
                        .map(CampingType::getPrice).orElse(BigDecimal.ZERO));
            }
        }
        return total;
    }

    public void removeTicket(OrderRequestDTO request, int index) {
        if (request.getTickets() != null && index >= 0 && index < request.getTickets().size()) {
            request.getTickets().remove(index);
        }
    }

    public void removeCamping(OrderRequestDTO request, int index) {
        if (request.getCampings() != null && index >= 0 && index < request.getCampings().size()) {
            request.getCampings().remove(index);
        }
    }

    @Transactional
    public Order executeOrder(OrderRequestDTO request, String userEmail) {

        // Validamos que no intente comprar nada (ambas listas vacías)
        boolean hasTickets = request.getTickets() != null && !request.getTickets().isEmpty();
        boolean hasCampings = request.getCampings() != null && !request.getCampings().isEmpty();

        if (!hasTickets && !hasCampings) {
            throw new AppException("msg.validation.atLeastOne");
        }

        // Buscamos al usuario que realiza la compra
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AppException("msg.error.userNotFound"));

        // Creamos la cabecera del pedido
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PAID); // Asumimos pago inmediate por ahora
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Procesamos los tickets
        if (hasTickets) {
            for (TicketOrderDTO tDto : request.getTickets()) {
                TicketType type = ticketTypeRepository.findById(tDto.getTicketTypeId())
                        .orElseThrow(() -> new AppException("msg.error.ticketTypeNotFound"));

                // Validar stock
                if (type.getStockAvailable() <= 0) throw new AppException("msg.error.noStock", type.getName());

                // Crear entidad Ticket
                Ticket ticket = new Ticket();
                ticket.setFirstName(tDto.getFirstName());
                ticket.setLastName(tDto.getLastName());
                ticket.setDocumentType(tDto.getDocumentType());
                ticket.setDocumentNumber(tDto.getDocumentNumber());
                ticket.setBirthDate(tDto.getBirthDate());
                ticket.setTicketType(type);
                ticket.setQrCode(UUID.randomUUID().toString());
                ticket.setOrder(order);

                order.getTickets().add(ticket);
                totalPrice = totalPrice.add(type.getPrice());

                // Reducir stock
                type.setStockAvailable(type.getStockAvailable() - 1);
                ticketTypeRepository.save(type);
            }
        }

        // Procesamos el Camping
        if (hasCampings) {
            for (CampingOrderDTO cDto : request.getCampings()) {
                CampingType type = campingTypeRepository.findById(cDto.getCampingTypeId())
                        .orElseThrow(() -> new AppException("msg.error.campingTypeNotFound"));

                if (type.getStockAvailable() <= 0) {
                    throw new AppException("msg.error.noStock", type.getName());
                }

                Camping camping = new Camping();
                camping.setFirstName(cDto.getFirstName());
                camping.setLastName(cDto.getLastName());
                camping.setDocumentType(cDto.getDocumentType());
                camping.setDocumentNumber(cDto.getDocumentNumber());
                camping.setBirthDate(cDto.getBirthDate());
                camping.setCampingType(type);
                camping.setQrCode(UUID.randomUUID().toString());
                camping.setOrder(order);

                order.getCampings().add(camping);
                totalPrice = totalPrice.add(type.getPrice());

                type.setStockAvailable(type.getStockAvailable() - 1);
                campingTypeRepository.save(type);
            }
        }

        // Guardamos el pedido final con el precio total
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }
}


















