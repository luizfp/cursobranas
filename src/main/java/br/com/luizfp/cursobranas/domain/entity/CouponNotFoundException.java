package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

public final class CouponNotFoundException extends RuntimeException {
    @NotNull
    private final String couponCode;

    public CouponNotFoundException(@NotNull final String couponCode) {
        super("Coupon with code %s not found".formatted(couponCode));
        this.couponCode = couponCode;
    }
}
