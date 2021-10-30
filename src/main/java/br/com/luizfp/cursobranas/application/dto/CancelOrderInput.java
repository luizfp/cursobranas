package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

public record CancelOrderInput(@NotNull Long orderId) {
}
