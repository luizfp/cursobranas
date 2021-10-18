package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

public class StockItemNotFoundException extends RuntimeException {
    @NotNull
    private final Long itemId;

    public StockItemNotFoundException(@NotNull final Long itemId) {
        super("Stock item with ID %d not found".formatted(itemId));
        this.itemId = itemId;
    }
}
