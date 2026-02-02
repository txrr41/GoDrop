package com.project.shiphub.dto.payment;


import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;


@Data
public class CreatePaymentRequest {

    @NotNull(message = "Order ID é obrigatorio")
    private Long orderId;

    @NotNull(message = "Método de pagamento é obrigatório")
    private String paymentMethod;

    @Positive(message = "Valor deve ser positivo")
    private Long amountInCents;

    private String paymentMethodId;

    private String customerEmail;
}
