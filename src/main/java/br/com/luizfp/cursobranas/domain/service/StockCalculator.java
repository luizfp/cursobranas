package br.com.luizfp.cursobranas.domain.service;

import br.com.luizfp.cursobranas.domain.entity.StockEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class StockCalculator {

    public int countAvailableStockQuantity(@NotNull final Collection<StockEntry> entries) {
        return entries
                .stream()
                .mapToInt(entry -> entry.isIn() ? entry.quantity() : -entry.quantity())
                .sum();
    }
}
