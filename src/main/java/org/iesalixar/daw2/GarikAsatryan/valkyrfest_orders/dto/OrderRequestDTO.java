package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestDTO {
    private List<TicketOrderDTO> tickets = new ArrayList<>();
    private List<CampingOrderDTO> campings = new ArrayList<>();
}