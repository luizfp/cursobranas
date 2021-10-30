package br.com.luizfp.cursobranas.domain.factory;

import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import org.jetbrains.annotations.NotNull;

public interface AbstractRepositoryFactory {
    @NotNull
    StockItemRepository createStockItemRepository();
    @NotNull
    CouponRepository createCouponRepository();
    @NotNull
    OrderRepository createOrderRepository();
}
