package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

public final class OutOfStockException extends RuntimeException {
    @NotNull
    private final Long itemId;
    private final int availableQuantity;
    private final int desiredQuantity;

    public OutOfStockException(@NotNull final Long itemId,
                               final int availableQuantity,
                               final int desiredQuantity) {
        super("Error to buy item %d.\nAvailable quantity: %d\nDesired quantity: %d."
                      .formatted(itemId, availableQuantity, desiredQuantity));
        this.itemId = itemId;
        this.availableQuantity = availableQuantity;
        this.desiredQuantity = desiredQuantity;
    }
}
