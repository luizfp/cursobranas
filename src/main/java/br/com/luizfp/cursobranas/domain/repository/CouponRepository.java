package br.com.luizfp.cursobranas.domain.repository;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import org.jetbrains.annotations.NotNull;

public interface CouponRepository {

    @NotNull
    Coupon getByCode(@NotNull final String couponCode);
}
