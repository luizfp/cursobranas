package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.Coupon;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.google.common.truth.Truth.assertThat;

public class CouponTest {

    @Test
    void shouldCreateNonExpiredCoupon() {
        final Coupon coupon = new Coupon(1L, "10OFF", OffsetDateTime.parse("2021-10-01T10:00:00+00:00"), 0.10);
        final OffsetDateTime now = OffsetDateTime.parse("2021-09-01T10:00:00+00:00");
        assertThat(coupon.isExpired(now)).isFalse();
    }

    @Test
    void shouldCreateExpiredCoupon() {
        final Coupon coupon = new Coupon(1L, "10OFF", OffsetDateTime.parse("2021-08-01T10:00:00+00:00"), 0.10);
        final OffsetDateTime now = OffsetDateTime.parse("2021-09-01T10:00:00+00:00");
        assertThat(coupon.isExpired(now)).isTrue();
    }

    @Test
    void shouldCreateCouponThatNeverExpires() {
        final Coupon coupon = new Coupon(1L, "10OFF", null, 0.10);
        final OffsetDateTime now = OffsetDateTime.parse("2021-09-01T10:00:00+00:00");
        assertThat(coupon.isExpired(now)).isFalse();
    }
}
