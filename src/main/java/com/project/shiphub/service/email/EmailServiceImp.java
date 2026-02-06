package com.project.shiphub.service.email;

import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImp implements EmailService {
    private final JavaMailSender mailSender;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EmailServiceImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOrderConfirmationEmail(Order order, LocalDateTime paymentHour) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(order.getBuyerEmail());
        email.setSubject("✅ Confirmação de Pagamento - Pedido #" + order.getId());
        email.setText(buildEmailBody(order, paymentHour));

        mailSender.send(email);
    }

    @Async
    public void sendTrackingEmail(Order order) {
        try {
            Thread.sleep(Duration.ofMinutes(1).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(order.getBuyerEmail());
        email.setSubject("Seu pedido foi enviado! \uD83D\uDCE6");
        email.setText(buildTrackingEmail(order));

        mailSender.send(email);
    }

    private String buildTrackingEmail(Order order) {

        return "Olá, " + order.getBuyerName() + "!\n\n" +
                "Temos uma ótima notícia \uD83D\uDE0A\n\n" +
                "Seu pedido já foi enviado e está a caminho!\n\n" +
                "Confira abaixo o código de rastreio para acompanhar a entrega em tempo real:\n\n" +
                "Código de rastreio: #BRNE4MSQW\n\n" +
                "Você pode acompanhar o status da entrega diretamente no site da transportadora ou no nosso site na aba meus pedidos.\n\n" +
                "Se precisar de ajuda ou tiver qualquer dúvida, é só responder este e-mail. Estamos à disposição!\n\n" +
                "📧 Email: suportgodrop@gmail.com\n" +
                "📱 WhatsApp: (41) 99900-2380\n" +
                "🌐 Site: godrop.com.br\n\n" +
                "Agradecemos pela confiança! 🎉\n\n" +
                "Atenciosamente,\n" +
                "Equipe GoDrop";
    }

    private String buildEmailBody(Order order, LocalDateTime paymentHour) {
        StringBuilder body = new StringBuilder();

        body.append("Olá, ").append(order.getBuyerName()).append("!\n\n");
        body.append("Confirmamos o recebimento do seu pagamento!\n\n");

        body.append("📦 DETALHES DO PEDIDO #").append(order.getId()).append("\n");

        body.append("🛒 PRODUTOS COMPRADOS:\n\n");

        for (OrderItem item : order.getItems()) {
            body.append("  • ").append(item.getProduct().getNome()).append("\n");
            body.append("    Quantidade: ").append(item.getQuantity()).append(" un\n");
            body.append("    Preço unitário: R$ ").append(item.getUnitPrice()).append("\n");
            body.append("    Subtotal: R$ ").append(item.getTotalPrice()).append("\n\n");
        }
        body.append("\n");
        body.append("💰 RESUMO DO PAGAMENTO:\n\n");
        body.append("  Subtotal: R$ ").append(order.getTotalAmount()).append("\n");
        body.append("  Frete: GRÁTIS\n");
        body.append("  ─────────────────────────────\n");
        body.append("  TOTAL: R$ ").append(order.getTotalAmount()).append("\n\n");

        body.append("📅 Data do pagamento: ").append(paymentHour.format(formatter)).append("\n");
        body.append("🆔 Número do pedido: #").append(order.getId()).append("\n\n");

        body.append("\n");

        body.append("📍 ENDEREÇO DE ENTREGA:\n\n");
        body.append("  ").append(order.getBuyerName()).append("\n");
        body.append("  ").append(order.getShippingStreet()).append(", ").append(order.getShippingNumber());
        if (order.getShippingComplement() != null && !order.getShippingComplement().isEmpty()) {
            body.append(" - ").append(order.getShippingComplement());
        }
        body.append("\n");
        body.append("  ").append(order.getShippingNeighborhood()).append("\n");
        body.append("  ").append(order.getShippingCity()).append(" - ").append(order.getShippingState()).append("\n");
        body.append("  CEP: ").append(order.getShippingCep()).append("\n\n");

        body.append("\n");

        body.append("📦 PRÓXIMOS PASSOS:\n\n");
        body.append("1️⃣ Seu pedido será processado em até 24 horas\n");
        body.append("2️⃣ Você receberá o código de rastreio por email\n");
        body.append("3️⃣ Acompanhe sua entrega pelo nosso sistema\n\n");

        body.append("\n");

        body.append("Caso tenha dúvidas, nossa equipe está à disposição:\n\n");
        body.append("📧 Email: suportgodrop@gmail.com\n");
        body.append("📱 WhatsApp: (41) 99900-2380\n");
        body.append("🌐 Site: godrop.com.br\n\n");

        body.append("Agradecemos pela confiança! 🎉\n\n");
        body.append("Atenciosamente,\n");
        body.append("Equipe GoDrop");

        return body.toString();
    }
}