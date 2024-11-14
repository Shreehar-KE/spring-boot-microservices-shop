package com.shreehar.microservices.order.service;

import com.shreehar.microservices.order.client.InventoryClient;
import com.shreehar.microservices.order.dto.OrderRequest;
import com.shreehar.microservices.order.model.Order;
import com.shreehar.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {

        boolean isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if (isInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());
            orderRepository.save(order);
        } else  {
            throw new RuntimeException("Product with Skucode "+ orderRequest.skuCode() + " is not in stock for this order");
        }
    }
}
