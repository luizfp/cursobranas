package br.com.luizfp.cursobranas.domain.repository;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface CouponRepository {

    @NotNull
    Optional<Coupon> getByCode(@NotNull final String couponCode);
}
