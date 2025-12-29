package org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Camping;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Order;
import org.iesalixar.daw2.GarikAsatryan.valkyrfest_orders.entities.Ticket;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final QrCodeService qrCodeService;

    public byte[] generateOrderPdf(Order order) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        document.open();

        // Título principal
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Paragraph title = new Paragraph("VALKYRFEST - CREDENCIALES", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Espacio

        // Información del Pedido
        document.add(new Paragraph("Pedido ID: " + order.getId()));
        document.add(new Paragraph("Explorador: " + order.getUser().getFirstName() + " " + order.getUser().getLastName()));
        document.add(new Paragraph(" "));

        // Generar una "tarjeta" por cada Ticket
        for (Ticket ticket : order.getTickets()) {
            addEntryToPdf(document, "ENTRADA: " + ticket.getTicketType().getName(),
                    ticket.getFirstName() + " " + ticket.getLastName(),
                    ticket.getQrCode());
        }

        // Generar una "tarjeta" por cada Reserva de Camping
        for (Camping camping : order.getCampings()) {
            addEntryToPdf(document, "CAMPING: " + camping.getCampingType().getName(),
                    camping.getFirstName() + " " + camping.getLastName(),
                    camping.getQrCode());
        }

        document.close();
        return out.toByteArray();
    }

    private void addEntryToPdf(Document document, String type, String name, String qrData) throws Exception {
        document.add(new Paragraph("-----------------------------------------------------------------------"));
        document.add(new Paragraph(type, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        document.add(new Paragraph("Asistente: " + name));

        // Añadir el QR generado
        byte[] qrBytes = qrCodeService.generateQrCodeImage(qrData);
        Image qrImage = Image.getInstance(qrBytes);
        qrImage.scaleToFit(100, 100);
        document.add(qrImage);
    }
}