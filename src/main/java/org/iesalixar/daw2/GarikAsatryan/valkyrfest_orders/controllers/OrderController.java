package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.OrderRequestDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.CampingType;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.TicketType;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.User;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.exceptions.AppException; // Importación necesaria
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final TicketTypeService ticketTypeService;
    private final CampingTypeService campingTypeService;
    private final PdfGeneratorService pdfGeneratorService;
    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping
    public String showOrderForm(Model model, HttpSession session) {
        OrderRequestDTO request = (OrderRequestDTO) session.getAttribute("pendingOrder");
        if (request == null) request = new OrderRequestDTO();

        model.addAttribute("orderRequest", request);
        model.addAttribute("ticketTypes", ticketTypeService.getAllTicketTypes());
        model.addAttribute("campingTypes", campingTypeService.getAllCampingTypes());
        return "order/form";
    }

    @PostMapping
    public String processToCheckout(@ModelAttribute OrderRequestDTO orderRequest, HttpSession session) {
        session.setAttribute("pendingOrder", orderRequest);
        return "redirect:/order/checkout";
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        OrderRequestDTO request = (OrderRequestDTO) session.getAttribute("pendingOrder");

        if (request == null || (request.getTickets().isEmpty() && request.getCampings().isEmpty())) {
            return "redirect:/order";
        }

        BigDecimal total = orderService.calculateTotal(request);
        model.addAttribute("orderRequest", request);
        model.addAttribute("totalPrice", total);

        model.addAttribute("ticketTypesMap", ticketTypeService.getAllTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, t -> t)));
        model.addAttribute("campingTypesMap", campingTypeService.getAllCampingTypes().stream()
                .collect(Collectors.toMap(CampingType::getId, c -> c)));

        return "order/checkout";
    }

    @GetMapping("/my-orders")
    public String showMyOrders(Authentication authentication, Model model) {
        String email = authentication.getName();
        List<Order> myOrders = orderService.getOrdersByUser(email);
        model.addAttribute("orders", myOrders);
        return "order/my-orders";
    }

    @GetMapping("/remove/{type}/{index}")
    public String removeItem(@PathVariable String type, @PathVariable int index, HttpSession session) {
        OrderRequestDTO request = (OrderRequestDTO) session.getAttribute("pendingOrder");
        if (request != null) {
            if ("ticket".equals(type)) {
                orderService.removeTicket(request, index);
            } else if ("camping".equals(type)) {
                orderService.removeCamping(request, index);
            }
        }
        return "redirect:/order/checkout";
    }

    @PostMapping("/confirm")
    public String confirmOrder(HttpSession session, Authentication authentication) throws Exception {
        OrderRequestDTO request = (OrderRequestDTO) session.getAttribute("pendingOrder");
        if (request == null) return "redirect:/order";

        User user = userService.getUserByEmail(authentication.getName());
        Order order = orderService.executeOrder(request, user);
        String stripeUrl = paymentService.createStripeSession(order);
        session.removeAttribute("pendingOrder");

        return "redirect:" + stripeUrl;
    }

    @GetMapping("/success/{id}")
    public String showSuccess(@PathVariable Long id, Model model, Authentication authentication) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new AppException("msg.error.orderNotFound"));

        if (!order.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/";
        }

        model.addAttribute("order", order);
        return "order/success";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, Authentication authentication) throws Exception {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new AppException("msg.error.orderNotFound"));

        if (!order.getUser().getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        byte[] pdfBytes = pdfGeneratorService.generateOrderPdf(order);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Valkyrfest_Pedido_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}