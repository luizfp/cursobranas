package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record OrderItem(@NotNull Long id,
                        @NotNull String category,
                        @NotNull String description,
                        @NotNull BigDecimal price,
                        double heightCm,
                        double widthCm,
                        double lengthCm,
                        double weightKg,
                        int quantity) {

    @NotNull
    public BigDecimal calculateTotal() {
        return price.multiply(new BigDecimal(quantity));
    }
}
