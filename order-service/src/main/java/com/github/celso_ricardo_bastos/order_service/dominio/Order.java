package com.github.celso_ricardo_bastos.order_service.dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {

    private final String orderId;
    private final String customerId;
    private final BigDecimal totalAmount;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private final String cep;


    /**
     * Construtor para a criação de um NOVO pedido.
     * Gera automaticamente o orderId (UUID), o status inicial ("PENDING") e a data de criação.
     *
     * @param customerId ID do cliente que está realizando o pedido
     * @param totalAmount Valor total do pedido
     */
    public Order(String customerId, BigDecimal totalAmount, OrderStatus status, String cep) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("O ID do cliente é obrigatório.");
        }
        if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor total do pedido deve ser maior que zero.");
        }

        this.orderId = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.cep = cep;
    }

    // Ponto de entrada de criação (Sem usar 'new' no seu Service)
    public static Order create(String customerId, BigDecimal totalAmount, OrderStatus status, String cep) {
        return new Order(customerId, totalAmount, status, cep);
    }

    /**
     * Construtor secundário para reconstituir um pedido existente a partir do banco de dados ou DTO.
     */
    public Order(String orderId, String customerId, BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt, String cep, String off) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.cep = cep;
    }

    // Regras de negócio do domínio (Comportamentos)
    public void updateStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio.");
        }
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCep() {
        return cep;
    }
}
