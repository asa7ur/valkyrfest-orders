package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.dto.OrderRequestDTO;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.CampingType;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.TicketType;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.User;
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
    // NUEVAS INYECCIONES
    private final UserService userService;
    private final PaymentService paymentService;

    // 1. Mostrar el formulario inicial
    @GetMapping
    public String showOrderForm(Model model, HttpSession session) {
        OrderRequestDTO request = (OrderRequestDTO) session.getAttribute("pendingOrder");
        if (request == null) request = new OrderRequestDTO();

        model.addAttribute("orderRequest", request);
        model.addAttribute("ticketTypes", ticketTypeService.getAllTicketTypes());
        model.addAttribute("campingTypes", campingTypeService.getAllCampingTypes());
        return "order/form";
    }

    // 2. Recibir datos del formulario y enviarlos al Checkout (Sesión)
    @PostMapping
    public String processToCheckout(@ModelAttribute OrderRequestDTO orderRequest, HttpSession session) {
        session.setAttribute("pendingOrder", orderRequest);
        return "redirect:/order/checkout";
    }

    // 3. Mostrar la página de Checkout (Resumen)
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

    // 4. Eliminar items desde el checkout
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

    // 5. Confirmación final, creación del pedido y redirección a Stripe
    @PostMapping("/confirm")
    public String confirmOrder(HttpSession session, Authentication authentication) throws Exception {
        OrderRequestDTO request = (OrderRequestDTO) session.getAttribute("pendingOrder");
        if (request == null) return "redirect:/order";

        // Obtenemos el usuario autenticado como Objeto User
        User user = userService.getUserByEmail(authentication.getName());

        // Creamos el pedido (esto ahora requiere el objeto User)
        Order order = orderService.executeOrder(request, user);

        // Generamos la URL de Stripe para este pedido
        String stripeUrl = paymentService.createStripeSession(order);

        // Limpiamos la sesión una vez creado el pedido
        session.removeAttribute("pendingOrder");

        // Redirigimos a la pasarela externa de Stripe
        return "redirect:" + stripeUrl;
    }

    // 6. Pantalla de éxito (Stripe nos enviará aquí al terminar)
    @GetMapping("/success/{id}")
    public String showSuccess(@PathVariable Long id, Model model, Authentication authentication) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!order.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/";
        }

        model.addAttribute("order", order);
        return "order/success";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, Authentication authentication) throws Exception {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

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