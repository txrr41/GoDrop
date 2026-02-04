package com.project.shiphub.dto.payment;


import jakarta.validation.constraints.Positive;
import lombok.Data;
import jakarta.validation.constraints.NotNull;


@Data
public class CreatePaymentRequest {

    private Long orderId;

    @NotNull(message = "Método de pagamento é obrigatório")
    private String paymentMethod;

    @Positive(message = "Valor deve ser positivo")
    private Long amountInCents;

    private String paymentMethodId;

    private String customerEmail;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String buyerCpf;
    private String shippingCep;
    private String shippingStreet;
    private String shippingNumber;
    private String shippingComplement;
    private String shippingNeighborhood;
    private String shippingCity;
    private String shippingState;
}
