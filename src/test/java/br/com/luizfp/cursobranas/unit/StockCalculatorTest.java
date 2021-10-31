package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.StockEntry;
import br.com.luizfp.cursobranas.domain.service.StockCalculator;
import br.com.luizfp.cursobranas.domain.service.StockCalculatorException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static br.com.luizfp.cursobranas.domain.entity.StockEntryOperation.IN;
import static br.com.luizfp.cursobranas.domain.entity.StockEntryOperation.OUT;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockCalculatorTest {
    private static StockCalculator stockCalculator;

    @BeforeAll
    static void beforeAll() {
        stockCalculator = new StockCalculator();
    }

    @Test
    void shouldCalculateZeroStockItems() {
        final var entries = List.of(new StockEntry(1L, IN, 10),
                                    new StockEntry(1L, OUT, 10));
        assertThat(stockCalculator.countAvailableStockQuantity(entries)).isEqualTo(0);
    }

    @Test
    void shouldCalculateNegativeStockQuantity() {
        final var entries = List.of(new StockEntry(1L, IN, 10),
                                    new StockEntry(1L, OUT, 15));
        assertThat(stockCalculator.countAvailableStockQuantity(entries)).isEqualTo(-5);
    }

    @Test
    void shouldFailWithDifferentStockItems() {
        final var entries = List.of(new StockEntry(1L, IN, 10),
                                    new StockEntry(2L, IN, 15));
        assertThrows(StockCalculatorException.class, () -> stockCalculator.countAvailableStockQuantity(entries));
    }
}
