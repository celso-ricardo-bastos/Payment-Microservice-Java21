package com.github.celso_ricardo_bastos.payment_service.dominio.services;

import com.github.celso_ricardo_bastos.payment_service.application.dto.CreatePaymentCommand;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.Payment;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PendingPaymentStrategy extends PaymentStatusStrategy {
    @Override
    public PaymentStatus status() {
        return PaymentStatus.PENDING;
    }
    @Override
    public int off() {
        return 50;
    }
}