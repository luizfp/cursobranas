package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

public record StockEntry(@NotNull Long itemId,
                         @NotNull StockEntryOperation operation,
                         int quantity) {
}
