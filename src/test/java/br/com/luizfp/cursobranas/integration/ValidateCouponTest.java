package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.usecase.ValidateCoupon;
import br.com.luizfp.cursobranas.domain.entity.Coupon;
import br.com.luizfp.cursobranas.domain.entity.CouponNotFoundException;
import br.com.luizfp.cursobranas.domain.entity.ExpiredCouponException;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ValidateCouponTest {
    @Mock
    private AbstractRepositoryFactory repositoryFactory;

    @BeforeEach
    void beforeEach() {
        final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
        Mockito.when(repositoryFactory.createCouponRepository()).thenReturn(couponRepository);
    }

    @Test
    void shouldValidateACoupon() {
        final Coupon coupon = new Coupon(1L, "10OFF", OffsetDateTime.parse("2021-02-01T10:00:00+00:00"), 0.1);
        Mockito.when(repositoryFactory.createCouponRepository().getByCode("10OFF")).thenReturn(coupon);
        final ValidateCoupon validateCoupon = new ValidateCoupon(repositoryFactory);
        final OffsetDateTime now = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        assertDoesNotThrow(() -> validateCoupon.execute("10OFF", now));
    }

    @Test
    void shouldThrowsNotFoundCoupon() {
        Mockito.when(repositoryFactory.createCouponRepository().getByCode("NOT_FOUND"))
                .thenThrow(new CouponNotFoundException("NOT_FOUND"));
        final ValidateCoupon validateCoupon = new ValidateCoupon(repositoryFactory);
        final OffsetDateTime now = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        assertThrows(CouponNotFoundException.class, () -> validateCoupon.execute("NOT_FOUND", now));
    }

    @Test
    void shouldThrowsExpiredCoupon() {
        final Coupon coupon = new Coupon(1L, "OVERDUE", OffsetDateTime.parse("2020-01-01T10:00:00+00:00"), 0.1);
        Mockito.when(repositoryFactory.createCouponRepository().getByCode("OVERDUE")).thenReturn(coupon);
        final ValidateCoupon validateCoupon = new ValidateCoupon(repositoryFactory);
        final OffsetDateTime now = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        assertThrows(ExpiredCouponException.class, () -> validateCoupon.execute("OVERDUE", now));
    }
}
