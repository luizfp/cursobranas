package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static com.google.common.truth.Truth.assertThat;

public final class OrderTest {
    public static final String CPF = "584.876.259-75";

    @Test
    void shouldCreateAnOrder() {
        assertThat(new Order(CPF, OffsetDateTime.now(), 1)).isNotNull();
    }

    @Test
    void shouldThrowsErrorWithInvalidCpf() {
        Assertions.assertThrows(InvalidCpfException.class,
                                () -> new Order("111.111.111-11", OffsetDateTime.now(), 1));
    }

    @Test
    void shouldCreatOrderWithRightTotalItems() {
        final Order order = new Order(CPF, OffsetDateTime.now(), 1);
        order.addItem(new StockItem(1L, "Electronics", "Mouse", new BigDecimal("50.0"), 10, 2,  3, 5, 0.3), 5);
        order.addItem(new StockItem(2L, "Electronics", "Keyboard", new BigDecimal("200.0"), 10, 2, 30, 10, 0.5), 2);
        order.addItem(new StockItem(3L, "Electronics", "Smartphone", new BigDecimal("800.0"), 10, 2, 3, 5, 0.3), 1);
        assertThat(order.getTotalItems()).isEqualTo(8);
    }

    @Test
    void shouldCreatOrderWithRightAmountExpended() {
        final Order order = new Order(CPF, OffsetDateTime.now(), 1);
        order.addItem(new StockItem(1L, "Electronics", "Mouse", new BigDecimal("50.0"), 10, 2, 3, 5, 0.3), 5);
        order.addItem(new StockItem(2L, "Electronics", "Keyboard", new BigDecimal("200.0"), 10, 2, 30, 10, 0.5), 2);
        order.addItem(new StockItem(3L, "Electronics", "Smartphone", new BigDecimal("800.0"), 10, 2, 3, 5, 0.3), 1);
        assertThat(order.calculateOrderTotal()).isEqualTo(BigDecimal.valueOf(1450.0));
    }

    @Test
    void shouldCreatOrderWithTenPercentCoupon() {
        final Order order = new Order(CPF, OffsetDateTime.now(), 1);
        order.addItem(new StockItem(1L, "Electronics", "Mouse", new BigDecimal("50.0"), 10, 2, 3, 5, 0.3), 5);
        order.addItem(new StockItem(2L, "Electronics", "Keyboard", new BigDecimal("200.0"), 10, 2, 30, 10, 0.5), 2);
        order.addItem(new StockItem(3L, "Electronics", "Smartphone", new BigDecimal("800.0"), 10, 2, 3, 5, 0.3), 1);
        final Coupon coupon = new Coupon(1L, "10OFF", OffsetDateTime.parse("2021-10-01T10:00:00+00:00"), 0.10);
        final OffsetDateTime now = OffsetDateTime.parse("2021-09-01T10:00:00+00:00");
        order.applyCoupon(coupon, now);
        assertThat(order.calculateOrderTotal()).isEqualTo(new BigDecimal("1305.00"));
    }

    @Test
    void shouldThrowsWithExpiredCoupon() {
        final Order order = new Order(CPF, OffsetDateTime.now(), 1);
        order.addItem(new StockItem(1L, "Electronics", "Mouse", new BigDecimal("50.0"), 10, 2, 3, 5, 0.3), 5);
        final Coupon coupon = new Coupon(1L, "10OFF", OffsetDateTime.parse("2021-08-01T10:00:00+00:00"), 0.10);
        final OffsetDateTime now = OffsetDateTime.parse("2021-09-01T10:00:00+00:00");
        Assertions.assertThrows(ExpiredCouponException.class, () -> order.applyCoupon(coupon, now));
    }

    @Test
    void shouldCalculateRightShippingCost() {
        final Order order = new Order(CPF, OffsetDateTime.now(), 1);
        order.addItem(new StockItem(1L, "Electronics", "Mouse", new BigDecimal("50.0"), 10, 2, 3, 5, 0.3), 2);
        order.addItem(new StockItem(1L, "Electronics", "Computer", new BigDecimal("1500"), 10, 2, 30, 25, 1.5), 1);
        assertThat(order.calculateShippingCost(1000)).isEqualTo(21);
    }
}
