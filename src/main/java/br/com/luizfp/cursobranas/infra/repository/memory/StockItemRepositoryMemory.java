package br.com.luizfp.cursobranas.infra.repository.memory;

import br.com.luizfp.cursobranas.domain.entity.StockItem;
import br.com.luizfp.cursobranas.domain.entity.StockItemNotFoundException;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class StockItemRepositoryMemory implements StockItemRepository {
    @NotNull
    private final List<StockItem> items = new ArrayList<>();

    public StockItemRepositoryMemory() {
        items.add(new StockItem(1L, "Electronics", "Mouse", new BigDecimal("50.0"),2, 3, 5, 0.3));
        items.add(new StockItem(2L, "Electronics", "Keyboard", new BigDecimal("200.0"), 2, 30, 10, 0.5));
        items.add(new StockItem(3L, "Electronics", "Smartphone", new BigDecimal("800.0"),2, 3, 5, 0.3));
    }

    @NotNull
    @Override
    public StockItem getById(@NotNull final Long itemId) {
        return items
                .stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new StockItemNotFoundException(itemId));
    }
}
