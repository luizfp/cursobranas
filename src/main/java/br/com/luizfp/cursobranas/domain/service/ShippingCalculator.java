package br.com.luizfp.cursobranas.domain.service;

import br.com.luizfp.cursobranas.domain.entity.ShippedItem;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public final class ShippingCalculator {
    public static final double MIN_SHIPPING_COST = 10;
    @NotNull
    private final List<ShippedItem> shippedItems;
    private final double shippingDistanceKm;

    public ShippingCalculator(final double shippingDistanceKm) {
        this.shippedItems = new ArrayList<>();
        this.shippingDistanceKm = shippingDistanceKm;
    }

    public void addShippedItem(@NotNull final ShippedItem shippedItem) {
        shippedItems.add(shippedItem);
    }

    public double calculateCost() {
        BigDecimal totalShipping = new BigDecimal(BigInteger.ZERO);
        for (final ShippedItem item : shippedItems) {
            final double volumeM3 = item.calculateVolumeInCubicMeters();
            final double density = item.calculateDensity();
            totalShipping = totalShipping.add(new BigDecimal(shippingDistanceKm * volumeM3 * (density / 100)));
        }
        final double roundedValue = totalShipping.setScale(2, RoundingMode.HALF_UP).doubleValue();
        return Math.max(roundedValue, MIN_SHIPPING_COST);
    }
}
