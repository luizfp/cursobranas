package br.com.luizfp.cursobranas.domain.repository;

import br.com.luizfp.cursobranas.domain.entity.StockEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface StockEntryRepository {
    void save(@NotNull final StockEntry stockEntry);

    @NotNull
    Collection<StockEntry> getByItemId(@NotNull final Long itemId);

    void clean();
}
