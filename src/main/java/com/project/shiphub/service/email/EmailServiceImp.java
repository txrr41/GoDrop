package com.project.shiphub.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EmailServiceImp implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String name, String product, BigDecimal totalAmount, String paymentMethod, LocalDateTime paymentHour) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject("Confirmação de pagamento");
        email.setText("Olá," + name + ",\n "+
                "\n" +
                "Esperamos que esteja tudo bem.\n" +
                "\n" +
                "Confirmamos o recebimento do seu pagamento referente a" + product + ", realizado em" + paymentHour + ", no valor de R$" + totalAmount +" .\n" +
                "\n" +
                "Detalhes do pagamento:\n" +
                "\n" +
                "Forma de pagamento: " + paymentMethod + "\n" +
                "\n" +
                "Número da transação ou referência: [código]\n" +
                "\n" +
                "Seu pagamento foi processado com sucesso e, A partir de agora, seu pedido seguirá para o processamento e envio. Assim que a postagem for realizada, você receberá em seu e-mail o código de rastreio, .\n" +
                "\n" +
                "para que possa acompanhar a entrega do seu pedido.\n" +
                "\n" +
                "Caso tenha qualquer dúvida ou precise de mais informações, nossa equipe está à disposição para ajudar. Basta responder a este e-mail ou entrar em contato pelo [canal de atendimento].\n" +
                "\n" +
                "Agradecemos pela confiança!\n" +
                "\n" +
                "Atenciosamente,\n" +
                "GoDrop\n" +
                "[(41)99900-2380 | godrop.com.br | suportgodrop@gmail.com]");

        mailSender.send(email);
    }


}
