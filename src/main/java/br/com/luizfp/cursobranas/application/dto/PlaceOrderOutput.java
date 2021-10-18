package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

public record PlaceOrderOutput(long orderId,
                               @NotNull String orderCode,
                               double orderTotal) {
}
