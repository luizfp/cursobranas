package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.StockItem;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

public final class StockItemRepositoryDatabase implements StockItemRepository {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public StockItemRepositoryDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public StockItem getById(@NotNull final Long itemId) {
        final DatabaseResultRow row = databaseConnection.one("select * from stock_item where id = ?", itemId);
        return new StockItem(
                row.get("id"),
                row.get("category"),
                row.get("description"),
                row.get("price"),
                row.get("quantity_available"),
                row.get("height_cm"),
                row.get("width_cm"),
                row.get("length_cm"),
                row.get("weight_kg"));
    }
}
