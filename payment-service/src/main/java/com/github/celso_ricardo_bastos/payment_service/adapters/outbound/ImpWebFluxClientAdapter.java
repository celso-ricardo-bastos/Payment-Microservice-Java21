package com.github.celso_ricardo_bastos.payment_service.adapters.outbound;

import com.github.celso_ricardo_bastos.payment_service.adapters.outbound.economia.EconomiaResponse;
import com.github.celso_ricardo_bastos.payment_service.adapters.outbound.economia.EconomiaWebFluxClient;
import com.github.celso_ricardo_bastos.payment_service.adapters.outbound.viacep.AddressWebFluxClient;
import com.github.celso_ricardo_bastos.payment_service.adapters.outbound.viacep.ViaCepResponse;
import com.github.celso_ricardo_bastos.payment_service.application.dto.CreatePaymentCommand;
import com.github.celso_ricardo_bastos.payment_service.application.dto.DataApisExternal;
import com.github.celso_ricardo_bastos.payment_service.application.ports.outbound.CreatePaymentOutboundPort;
import com.github.celso_ricardo_bastos.payment_service.application.usecase.CreatePaymentUserCase;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.Address;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.Economia;
import com.github.celso_ricardo_bastos.payment_service.dominio.model.Payment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;

/**
 * Adapter responsável pela comunicação com o ViaCEP.
 *
 * Ele implementará futuramente o nosso Outbound Port.
 *
 * Neste momento estamos focando apenas na comunicação HTTP.
 */
@Component
@RequiredArgsConstructor
public class ImpWebFluxClientAdapter implements CreatePaymentOutboundPort {
    private static final Logger log = LoggerFactory.getLogger(ImpWebFluxClientAdapter.class);
    private final AddressWebFluxClient clientAdress;
    private final EconomiaWebFluxClient clientExchange;

    /**
     * Executa as duas chamadas externas.
     */
    public Mono<DataApisExternal> execute(String cep) {

        /*
         * Apenas criamos a operação.
         */
        Mono<ViaCepResponse> address = clientAdress.findByCep(cep);
        Mono<EconomiaResponse> exchangeRate = clientExchange.findEconomia();

        log.info("Executando as duas APIs de forma simultanea 'Async'");
        /*
         * Mono.zip combina os resultados das duas operações.
         */
        return Mono.zip(
                        address,
                        exchangeRate
                )
                .map(tuple -> {

                    ViaCepResponse addressResult = tuple.getT1();
                    EconomiaResponse exchangeRateResult = tuple.getT2();
                    return new DataApisExternal(
                            new Address(
                                    addressResult.cep(),
                                    addressResult.logradouro(),
                                    addressResult.regiao(),
                                    addressResult.uf(),
                                    addressResult.estado()
                            ),
                            new Economia(
                                    exchangeRateResult.USDBRL().bid(),
                                    exchangeRateResult.USDBRL().timestamp(),
                                    new Economia(
                                            exchangeRateResult.USDBRL().bid(),
                                            exchangeRateResult.USDBRL().timestamp(),
                                            LocalDateTime.parse(
                                                    exchangeRateResult.USDBRL().create_date(),
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                            )
                                    ).create_date()
                            )
                    );
                });
    }

    public Mono<Payment> createPayment(CreatePaymentCommand command) {
        return this.execute(command.cep())
            .map(dataApis -> {
                Payment payment = Payment.create(
                        command.orderId(),
                        command.customerId(),
                        command.totalAmount(),
                        command.status(),
                        command.createdAt(),
                        dataApis.address(),
                        dataApis.economia()
                );
                return payment;
            });
    }
}