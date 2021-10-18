package br.com.luizfp.cursobranas.domain.repository;

import br.com.luizfp.cursobranas.domain.entity.StockItem;
import org.jetbrains.annotations.NotNull;

public interface StockItemRepository {

    @NotNull
    StockItem getById(@NotNull final Long itemId);
}
