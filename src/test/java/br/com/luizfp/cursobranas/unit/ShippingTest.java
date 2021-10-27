package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.ShippedItem;
import br.com.luizfp.cursobranas.domain.service.ShippingCalculator;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ShippingTest {

    @Test
    void shouldCalculateShippingCostWithOneItem() {
        final ShippingCalculator shipping = new ShippingCalculator(1000);
        shipping.addShippedItem(new ShippedItem(2, 30, 25, 1.5));
        assertThat(shipping.calculateCost()).isEqualTo(15);
    }

    @Test
    void shouldCalculateShippingCostWithMultipleItems() {
        final ShippingCalculator shipping = new ShippingCalculator(1000);
        shipping.addShippedItem(new ShippedItem(2, 3, 5, 0.3));
        shipping.addShippedItem(new ShippedItem(2, 3, 5, 0.3));
        shipping.addShippedItem(new ShippedItem(2, 30, 25, 1.5));
        assertThat(shipping.calculateCost()).isEqualTo(21);
    }

    @Test
    void shouldReturnMinimumShippingCost() {
        final ShippingCalculator shipping = new ShippingCalculator(1000);
        shipping.addShippedItem(new ShippedItem(2, 3, 5, 0.3));
        assertThat(shipping.calculateCost()).isEqualTo(ShippingCalculator.MIN_SHIPPING_COST);
    }
}
