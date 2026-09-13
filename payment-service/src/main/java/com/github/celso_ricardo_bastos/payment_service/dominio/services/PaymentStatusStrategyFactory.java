package com.github.celso_ricardo_bastos.payment_service.dominio.services;

import com.github.celso_ricardo_bastos.payment_service.dominio.model.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentStatusStrategyFactory {

    private final Map<PaymentStatus, PaymentStatusStrategy> strategies;

    public PaymentStatusStrategyFactory(
            List<PaymentStatusStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        PaymentStatusStrategy::status,
                        Function.identity()
                ));
    }

    public PaymentStatusStrategy get(PaymentStatus status) {

        PaymentStatusStrategy strategy = strategies.get(status);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Nenhuma strategy encontrada para: " + status
            );
        }

        return strategy;
    }
}
