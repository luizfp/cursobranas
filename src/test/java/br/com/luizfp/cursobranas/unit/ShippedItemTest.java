package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.ShippedItem;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ShippedItemTest {

    @Test
    void shouldCalculateVolumeInCubicMeters() {
        final ShippedItem itemMeasurements = new ShippedItem(2, 3, 5, 1);
        assertThat(itemMeasurements.calculateVolumeInCubicMeters()).isEqualTo(0.00003);
    }

    @Test
    void shouldCalculateDensity() {
        final ShippedItem itemMeasurements = new ShippedItem(2, 3, 5, 1);
        assertThat(itemMeasurements.calculateDensity()).isEqualTo(33333.333333333336);
    }
}
