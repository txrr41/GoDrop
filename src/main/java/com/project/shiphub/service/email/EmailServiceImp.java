package com.project.shiphub.service.email;

import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public EmailServiceImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOrderConfirmationEmail(Order order, LocalDateTime paymentHour) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(order.getBuyerEmail());
            helper.setSubject("✅ Pagamento Aprovado! Pedido #" + order.getId() + " está a caminho");
            helper.setText(buildOrderConfirmationHtml(order, paymentHour), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email de confirmação", e);
        }
    }

    @Async
    public void sendTrackingEmail(Order order) {
        try {
            Thread.sleep(Duration.ofSeconds(10).toMillis());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(order.getBuyerEmail());
            helper.setSubject("📦 Seu pedido foi enviado! Confira o rastreio");
            helper.setText(buildTrackingHtml(order), true);

            mailSender.send(message);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Erro ao enviar email de rastreio", e);
        }
    }

    private String buildTrackingHtml(Order order) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: 'Segoe UI', Arial, sans-serif; background: #f8fafc; margin: 0; padding: 0; }
                .wrapper { padding: 40px 10px; }
                .container { max-width: 600px; margin: auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
                .header { background: #0f172a; color: white; padding: 40px 20px; text-align: center; }
                .content { padding: 40px 30px; text-align: center; }
                .tracking-box { background: #f1f5f9; padding: 25px; border-radius: 12px; margin: 25px 0; border: 1px dashed #cbd5e1; }
                .tracking-code { font-family: monospace; font-size: 24px; color: #0f172a; font-weight: bold; letter-spacing: 2px; }
                .btn { background: #3b82f6; color: white !important; padding: 16px 30px; border-radius: 8px; text-decoration: none; font-weight: bold; display: inline-block; margin-top: 10px; }
                .footer { padding: 30px; background: #f8fafc; text-align: center; font-size: 13px; color: #64748b; }
            </style>
        </head>
        <body>
            <div class="wrapper">
                <div class="container">
                    <div class="header">
                        <style="margin:0; font-size: 24px;">GODROP</h1>
                    </div>
                    <div class="content">
                        <h2 style="color: #0f172a;">Olá, %s! 😊</h2>
                        <p style="color: #475569; font-size: 16px;">Seu pedido já foi enviado e está a caminho!</p>
                        
                        <div class="tracking-box">
                            <span style="display:block; font-size: 12px; color: #64748b; margin-bottom: 8px; text-transform: uppercase;">Código de Rastreio</span>
                            <span class="tracking-code">BRNE4MSQW</span>
                        </div>

                        <p style="color: #64748b; font-size: 14px; margin-bottom: 20px;">Você pode acompanhar a entrega pelo nosso site.</p>
                        <a href="https://godrop.com.br/rastreio" class="btn">Rastrear Pedido</a>
                    </div>
                    <div class="footer">
                        <p>📧 suportgodrop@gmail.com | 📱 (41) 99900-2380 | 🌐 godrop.com.br</p>
                        <p style="margin-top: 15px; font-weight: bold; color: #0f172a;">Equipe GoDrop</p>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """.formatted(order.getBuyerName());
    }

    private String buildOrderConfirmationHtml(Order order, LocalDateTime paymentHour) {
        StringBuilder itemsHtml = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsHtml.append("""
                <tr>
                    <td style="padding: 15px 0; border-bottom: 1px solid #f1f5f9;">
                        <span style="font-weight: 600; color: #1e293b; display: block;">%s</span>
                        <span style="font-size: 12px; color: #64748b;">Qtd: %d</span>
                    </td>
                    <td style="padding: 15px 0; border-bottom: 1px solid #f1f5f9; text-align: right; font-weight: 600; color: #1e293b;">
                        R$ %s
                    </td>
                </tr>
            """.formatted(
                    item.getProduct().getNome(),
                    item.getQuantity(),
                    item.getTotalPrice()
            ));
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: 'Segoe UI', Arial, sans-serif; background: #f8fafc; margin: 0; padding: 0; }
                .wrapper { padding: 40px 10px; }
                .container { max-width: 600px; margin: auto; background: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
                .header { background: #0f172a; color: white; padding: 40px 20px; text-align: center; }
                .content { padding: 40px 30px; }
                .item-table { width: 100%%; border-collapse: collapse; margin: 20px 0; }
                .address-box { background: #f8fafc; padding: 20px; border-radius: 12px; border-left: 4px solid #0f172a; margin-top: 30px; }
                .total-section { border-top: 2px solid #0f172a; padding-top: 15px; margin-top: 15px; }
                .footer { padding: 30px; background: #f8fafc; text-align: center; font-size: 13px; color: #64748b; }
            </style>
        </head>
        <body>
            <div class="wrapper">
                <div class="container">
                    <div class="header">
                        <h1 style="margin:0; font-size: 24px;">GODROP</h1>
                    </div>
                    <div class="content">
                        <h2 style="color: #0f172a; margin-top: 0;">Pagamento confirmado! ✅</h2>
                        <p style="color: #475569;">Olá <strong>%s</strong>, recebemos o pagamento do seu pedido.</p>
                        
                        <h3 style="font-size: 16px; color: #0f172a; margin-top: 25px;">📦 Pedido #%d</h3>
                        <table class="item-table">
                            %s
                        </table>

                        <div class="total-section">
                            <table width="100%%">
                                <tr>
                                    <td style="padding-top: 10px; font-size: 18px; font-weight: bold; color: #0f172a;">Total</td>
                                    <td style="padding-top: 10px; text-align: right; font-size: 22px; font-weight: bold; color: #0f172a;">R$ %s</td>
                                </tr>
                            </table>
                        </div>

                        <p style="font-size: 14px; color: #64748b; margin-top: 15px;">
                            <strong>Data do pagamento:</strong> %s <br>
                            <strong>Frete:</strong> GRÁTIS
                        </p>

                        <div class="address-box">
                            <h4 style="margin: 0 0 10px 0; color: #0f172a;">📍 Endereço de entrega</h4>
                            <p style="margin: 0; font-size: 14px; color: #475569; line-height: 1.5;">
                                %s <br>
                                %s, %s <br>
                                %s - %s <br>
                                CEP: %s
                            </p>
                        </div>
                    </div>
                    <div class="footer">
                        <p>📧 suportgodrop@gmail.com | 📱 (41) 99900-2380 | 🌐 godrop.com.br</p>
                        <p style="margin-top: 15px; font-weight: bold; color: #0f172a;">Equipe GoDrop</p>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                order.getBuyerName(),
                order.getId(),
                itemsHtml.toString(),
                order.getTotalAmount(),
                paymentHour.format(formatter),
                order.getBuyerName(),
                order.getShippingStreet(),
                order.getShippingNumber(),
                order.getShippingCity(),
                order.getShippingState(),
                order.getShippingCep()
        );
    }
}