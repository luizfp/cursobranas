package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final record GetOrderOutput(@NotNull Long orderId,
                                   @NotNull String orderCode,
                                   @NotNull List<GetOrderItemOutput> items) {
}
