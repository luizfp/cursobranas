package br.com.luizfp.cursobranas.domain.service;

import br.com.luizfp.cursobranas.domain.entity.StockEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class StockCalculator {

    public int countAvailableStockQuantity(@NotNull final Collection<StockEntry> entries) {
        final long distinctItemsCount = entries.stream().map(StockEntry::itemId).distinct().count();
        if (distinctItemsCount > 1) {
            throw new StockCalculatorException("Can't calculate stock quantity of multiple items at the same time");
        }
        return entries
                .stream()
                .mapToInt(entry -> entry.isIn() ? entry.quantity() : -entry.quantity())
                .sum();
    }
}
