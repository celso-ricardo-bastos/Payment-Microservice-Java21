package com.github.celso_ricardo_bastos.payment_service.dominio.services;

import com.github.celso_ricardo_bastos.payment_service.dominio.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CanceledPaymentStrategy extends PaymentStatusStrategy {
    @Override
    public PaymentStatus status() {
        return PaymentStatus.CANCELLED;
    }
    @Override
    public int off() {
        return 0;
    }
}