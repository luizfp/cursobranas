package br.com.luizfp.cursobranas.infra.factory;

import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.repository.database.CouponRepositoryDatabase;
import br.com.luizfp.cursobranas.infra.repository.database.OrderRepositoryDatabase;
import br.com.luizfp.cursobranas.infra.repository.database.StockItemRepositoryDatabase;
import org.jetbrains.annotations.NotNull;

public final class DatabaseRepositoryFactory implements AbstractRepositoryFactory {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public DatabaseRepositoryFactory(@NotNull  final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public StockItemRepository createStockItemRepository() {
        return new StockItemRepositoryDatabase(databaseConnection);
    }

    @NotNull
    @Override
    public CouponRepository createCouponRepository() {
        return new CouponRepositoryDatabase(databaseConnection);
    }

    @NotNull
    @Override
    public OrderRepository createOrderRepository() {
        return new OrderRepositoryDatabase(databaseConnection);
    }
}
