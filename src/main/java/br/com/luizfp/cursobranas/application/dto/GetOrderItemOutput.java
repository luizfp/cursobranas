package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public final record GetOrderItemOutput(@NotNull Long id,
                                       @NotNull String category,
                                       @NotNull String description,
                                       @NotNull BigDecimal price,
                                       double heightCm,
                                       double widthCm,
                                       double lengthCm,
                                       double weightKg,
                                       int quantity) {
}
