package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record StockItem(@NotNull Long id,
                        @NotNull String category,
                        @NotNull String description,
                        @NotNull BigDecimal price,
                        double heightCm,
                        double widthCm,
                        double lengthCm,
                        double weightKg) {
}
