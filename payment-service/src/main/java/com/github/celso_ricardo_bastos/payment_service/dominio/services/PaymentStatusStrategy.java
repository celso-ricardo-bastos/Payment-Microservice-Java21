package com.github.celso_ricardo_bastos.payment_service.dominio.services;

import com.github.celso_ricardo_bastos.payment_service.application.dto.CreatePaymentCommand;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.Payment;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.PaymentStatus;

import java.math.BigDecimal;

public abstract class PaymentStatusStrategy {
   public abstract PaymentStatus status();
   public abstract int off();
   public Payment applyOffTotalAmount(Payment payment) {
        payment.setTotalAmount(payment.getTotalAmount()
                .multiply(BigDecimal.ONE.subtract(
                        BigDecimal.valueOf(this.off()).divide(BigDecimal.valueOf(100))
                )));
        return payment;
    }
}
