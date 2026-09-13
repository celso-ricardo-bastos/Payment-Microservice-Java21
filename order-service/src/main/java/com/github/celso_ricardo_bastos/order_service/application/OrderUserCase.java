package com.github.celso_ricardo_bastos.order_service.application;

import com.github.celso_ricardo_bastos.order_service.adapter.inbound.rest.dto.OrderRequest;
import com.github.celso_ricardo_bastos.order_service.adapter.inbound.rest.dto.OrderResponse;
import com.github.celso_ricardo_bastos.order_service.application.mapper.OrderMapper;
import com.github.celso_ricardo_bastos.order_service.dominio.Order;
import com.github.celso_ricardo_bastos.order_service.dominio.OrderStatus;
import com.github.celso_ricardo_bastos.order_service.application.ports.inbound.OrderInboundPort;
import com.github.celso_ricardo_bastos.order_service.application.ports.outbound.OrderOutboundPort;
import com.github.celso_ricardo_bastos.order_service.dominio.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderUserCase implements OrderInboundPort {
    private final OrderMapper mapper;
    private final OrderOutboundPort orderOutboundPort;

    public OrderUserCase(OrderMapper mapper, OrderOutboundPort orderOutboundPort) {
        this.mapper = mapper;
        this.orderOutboundPort = orderOutboundPort;
    }

    @Override
    public OrderResponse publishOrder(OrderRequest orderRequest) {
        Order order = Order.create(orderRequest.customerId(), orderRequest.totalAmount(), OrderStatus.valueOf(orderRequest.status()), orderRequest.cep());
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt().toString(),
                order.getCep()
        );
        this.orderOutboundPort.publishOrder(orderCreatedEvent);

        return this.mapper.toResponse(order);
    }
}
