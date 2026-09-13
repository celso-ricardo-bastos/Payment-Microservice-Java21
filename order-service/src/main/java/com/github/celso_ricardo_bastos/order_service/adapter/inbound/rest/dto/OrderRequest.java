package com.github.celso_ricardo_bastos.order_service.adapter.inbound.rest.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
        @NotBlank
        String customerId,

        @NotNull
        @Positive
        BigDecimal totalAmount,

        @NotNull
        String cep,

        @NotNull
        String status
) {}