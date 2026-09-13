package com.github.celso_ricardo_bastos.payment_service.adapters.outbound.viacep;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Adapter responsável pela comunicação com o ViaCEP.
 *
 * Ele implementará futuramente o nosso Outbound Port.
 *
 * Neste momento estamos focando apenas na comunicação HTTP.
 */
@Component
@RequiredArgsConstructor
public class AddressWebFluxClientAdapter {
    private final AddressWebFluxClient client;
    public Mono<ViaCepResponse> getData(String cep) {
        return client.findByCep(cep);
    }
}