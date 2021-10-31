package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.StockEntry;
import br.com.luizfp.cursobranas.domain.entity.StockEntryOperation;
import br.com.luizfp.cursobranas.domain.repository.StockEntryRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public final class StockEntryRepositoryDatabase implements StockEntryRepository {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public StockEntryRepositoryDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public void save(@NotNull final StockEntry entry) {
        databaseConnection.none("""
                                        insert into stock_entry (item_id, operation, quantity)
                                        values (?, ?, ?)
                                        """, entry.itemId(), entry.operation(), entry.quantity());
    }

    @NotNull
    @Override
    public Collection<StockEntry> getByItemId(@NotNull final Long itemId) {
        final Collection<DatabaseResultRow> data =
                databaseConnection.many("select * from stock_entry se where se.item_id = ?", itemId);
        return data
                .stream()
                .map(db -> new StockEntry(db.get("item_id"),
                                          StockEntryOperation.valueOf(db.get("operation")),
                                          db.get("quantity")))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void clean() {
        databaseConnection.none("delete from stock_entry where id > 3");
    }
}
