package br.com.luizfp.cursobranas.domain.entity;

import br.com.luizfp.cursobranas.domain.service.ShippingCalculator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    @NotNull
    private final Cpf cpf;
    @NotNull
    private final OffsetDateTime createdAt;
    @NotNull
    private final List<OrderItem> items;
    @NotNull
    private final OrderCode orderCode;
    @Nullable
    private Coupon coupon;

    public Order(@NotNull final String cpf,
                 @NotNull final OffsetDateTime orderCreatedAt,
                 final long sequence) {
        this.cpf = new Cpf(cpf);
        this.createdAt = orderCreatedAt;
        this.items = new ArrayList<>();
        this.orderCode = new OrderCode(orderCreatedAt, sequence);
    }

    @NotNull
    public String getCpf() {
        return cpf.getRawCpf();
    }

    @Nullable
    public Long getCouponIdOrNull() {
        return coupon != null ? coupon.id() : null;
    }

    @NotNull
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @NotNull
    public List<OrderItem> getOrderItems() {
        return items;
    }

    @NotNull
    public String getOrderCode() {
        return orderCode.getValue();
    }

    public void addItem(@NotNull final StockItem stockItem, final int quantity) {
        items.add(new OrderItem(stockItem.id(),
                                stockItem.category(),
                                stockItem.description(),
                                stockItem.price(),
                                stockItem.heightCm(),
                                stockItem.widthCm(),
                                stockItem.lengthCm(),
                                stockItem.weightKg(),
                                quantity));
    }

    public int getTotalItems() {
        return items
                .stream()
                .mapToInt(OrderItem::quantity)
                .reduce(0, Integer::sum);
    }

    @NotNull
    public BigDecimal calculateOrderTotal() {
        final BigDecimal orderTotal = items
                .stream()
                .map(OrderItem::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (coupon != null) {
            return orderTotal.multiply(BigDecimal.valueOf(1.0 - coupon.percentageDiscount()));
        }
        return orderTotal;
    }

    public void applyCoupon(@NotNull final Coupon coupon,
                            @NotNull final OffsetDateTime now) {
        if (coupon.isExpired(now)) {
            throw new ExpiredCouponException();
        }
        this.coupon = coupon;
    }

    public double calculateShippingCost(final double shippingDistanceKm) {
        final ShippingCalculator shipping = new ShippingCalculator(shippingDistanceKm);
        for (final OrderItem item : items) {
            for (int i = 0; i < item.quantity(); i++) {
                shipping.addShippedItem(new ShippedItem(item.heightCm(), item.widthCm(), item.lengthCm(), item.weightKg()));
            }
        }
        return shipping.calculateCost();
    }
}
