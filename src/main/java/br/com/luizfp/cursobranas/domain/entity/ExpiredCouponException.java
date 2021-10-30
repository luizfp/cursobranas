package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

public final class ExpiredCouponException extends RuntimeException {
    @NotNull
    private final String couponCode;

    public ExpiredCouponException(@NotNull final String couponCode) {
        super("Coupon with code %s is expired".formatted(couponCode));
        this.couponCode = couponCode;
    }
}
