package br.com.luizfp.cursobranas.infra.factory;

import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockEntryRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import br.com.luizfp.cursobranas.infra.repository.memory.OrderRepositoryMemory;
import br.com.luizfp.cursobranas.infra.repository.memory.StockItemRepositoryMemory;
import org.jetbrains.annotations.NotNull;

public final class MemoryRepositoryFactory implements AbstractRepositoryFactory {
    
    @NotNull
    @Override
    public StockItemRepository createStockItemRepository() {
        return new StockItemRepositoryMemory();
    }

    @NotNull
    @Override
    public StockEntryRepository createStockEntryRepository() {
        throw new IllegalStateException();
    }

    @NotNull
    @Override
    public CouponRepository createCouponRepository() {
        throw new IllegalStateException();
    }

    @NotNull
    @Override
    public OrderRepository createOrderRepository() {
        return new OrderRepositoryMemory();
    }
}
