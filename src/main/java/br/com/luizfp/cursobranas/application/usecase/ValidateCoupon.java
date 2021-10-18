package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import br.com.luizfp.cursobranas.domain.entity.CouponNotFoundException;
import br.com.luizfp.cursobranas.domain.entity.ExpiredCouponException;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public final class ValidateCoupon {
    @NotNull
    private final CouponRepository couponRepository;

    public ValidateCoupon(@NotNull final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void execute(@NotNull final String couponCode,
                        @NotNull final OffsetDateTime now) {
        final Coupon coupon = couponRepository
                .getByCode(couponCode)
                .orElseThrow(() -> new CouponNotFoundException(couponCode));
        if (coupon.isExpired(now)) {
            throw new ExpiredCouponException();
        }
    }
}
