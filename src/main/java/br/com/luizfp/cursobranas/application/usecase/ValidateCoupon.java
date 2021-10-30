package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import br.com.luizfp.cursobranas.domain.entity.ExpiredCouponException;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public final class ValidateCoupon {
    @NotNull
    private final CouponRepository couponRepository;

    public ValidateCoupon(@NotNull final AbstractRepositoryFactory factory) {
        this.couponRepository = factory.createCouponRepository();
    }

    public void execute(@NotNull final String couponCode,
                        @NotNull final OffsetDateTime now) {
        final Coupon coupon = couponRepository.getByCode(couponCode);
        if (coupon.isExpired(now)) {
            throw new ExpiredCouponException(couponCode);
        }
    }
}
