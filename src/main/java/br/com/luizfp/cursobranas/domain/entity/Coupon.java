package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.OffsetDateTime;

public record Coupon(@NotNull Long id,
                     @NotNull String code,
                     @Nullable OffsetDateTime expiresAt,
                     double percentageDiscount) {

    public boolean isExpired(@NotNull final OffsetDateTime now) {
        return expiresAt != null && expiresAt.isBefore(now);
    }
}
