package com.github.celso_ricardo_bastos.payment_service.application.usecase;

import com.github.celso_ricardo_bastos.payment_service.application.ports.outbound.AddressLookupOutboundPort;
import com.github.celso_ricardo_bastos.payment_service.application.ports.outbound.CreatePaymentOutboundPort;
import com.github.celso_ricardo_bastos.payment_service.application.ports.outbound.EconomiaOutboundPort;
import com.github.celso_ricardo_bastos.payment_service.application.ports.outbound.PaymentPersistenceOutboundPort;
import com.github.celso_ricardo_bastos.payment_service.application.dto.CreatePaymentCommand;
import com.github.celso_ricardo_bastos.payment_service.application.ports.inbound.CreatePaymentInboundPort;
import com.github.celso_ricardo_bastos.payment_service.dominio.services.PaymentStatusStrategy;

import com.github.celso_ricardo_bastos.payment_service.dominio.model.Payment;

import com.github.celso_ricardo_bastos.payment_service.dominio.model.PaymentStatus;
import com.github.celso_ricardo_bastos.payment_service.dominio.services.PaymentStatusStrategyFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

@Service
public class CreatePaymentUserCase implements CreatePaymentInboundPort {
    private static final Logger log = LoggerFactory.getLogger(CreatePaymentUserCase.class);
    private final PaymentPersistenceOutboundPort paymentPersistencePort;
    private final AddressLookupOutboundPort addressLookupOutboundPort;
    private final EconomiaOutboundPort economiaOutboundPort;
    private final ExecutorService executor;
    private final CreatePaymentOutboundPort createPaymentOutboundPort;
    private final PaymentStatusStrategyFactory paymentStatusStrategyFactory;

    public CreatePaymentUserCase(
            PaymentPersistenceOutboundPort paymentPersistenceOutboundPort,
            AddressLookupOutboundPort addressLookupOutboundPort,
            EconomiaOutboundPort economiaOutboundPort,
            ExecutorService executor,
            CreatePaymentOutboundPort createPaymentOutboundPort,
            PaymentStatusStrategyFactory paymentStatusStrategyFactory
            ) {
        this.paymentPersistencePort = paymentPersistenceOutboundPort;
        this.addressLookupOutboundPort = addressLookupOutboundPort;
        this.economiaOutboundPort = economiaOutboundPort;
        this.executor = executor;
        this.createPaymentOutboundPort = createPaymentOutboundPort;
        this.paymentStatusStrategyFactory = paymentStatusStrategyFactory;
    }

    @Override
    public void executePayment(CreatePaymentCommand command) {
        log.info("Create payment");
         Mono<Payment> payment =
                this.createPaymentOutboundPort
                        .createPayment(command)
                        .map(p -> {
                            PaymentStatusStrategy strategy = this.paymentStatusStrategyFactory.get(command.status());
                            strategy.applyOffTotalAmount(p);
                            return p;
                        });
        log.info("Save payment");
        this.paymentPersistencePort.save(payment);
    }
}