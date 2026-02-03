package com.project.shiphub.service.order;

import com.project.shiphub.dto.payment.CreatePaymentRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.order.Order;
import com.project.shiphub.model.order.OrderStatus;
import com.project.shiphub.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;


    public Order createOrder(User user, BigDecimal totalAmount, CreatePaymentRequest request) {
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setBuyerName(request.getBuyerName());
        order.setBuyerEmail(request.getBuyerEmail());
        order.setBuyerPhone(request.getBuyerPhone());
        order.setBuyerCpf(request.getBuyerCpf());
        order.setShippingCep(request.getShippingCep());
        order.setShippingStreet(request.getShippingStreet());
        order.setShippingNumber(request.getShippingNumber());
        order.setShippingComplement(request.getShippingComplement());
        order.setShippingNeighborhood(request.getShippingNeighborhood());
        order.setShippingCity(request.getShippingCity());
        order.setShippingState(request.getShippingState());

        return orderRepository.save(order);
    }
}