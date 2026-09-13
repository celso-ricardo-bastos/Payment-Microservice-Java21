package com.github.celso_ricardo_bastos.payment_service.dominio.services;

import com.github.celso_ricardo_bastos.payment_service.dominio.model.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CompletedPaymentStrategy extends PaymentStatusStrategy {
    @Override
    public PaymentStatus status() {
        return PaymentStatus.COMPLETED;
    }

    @Override
    public int off() {
        return 15;
    }
}