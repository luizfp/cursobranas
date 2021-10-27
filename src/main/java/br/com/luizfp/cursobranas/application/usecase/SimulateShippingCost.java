package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostInput;
import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostOutput;
import br.com.luizfp.cursobranas.domain.entity.ShippedItem;
import br.com.luizfp.cursobranas.domain.service.ShippingCalculator;
import br.com.luizfp.cursobranas.domain.entity.StockItem;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import org.jetbrains.annotations.NotNull;

public final class SimulateShippingCost {
    @NotNull
    private final StockItemRepository repository;

    public SimulateShippingCost(@NotNull final StockItemRepository repository) {
        this.repository = repository;
    }

    @NotNull
    public SimulateShippingCostOutput execute(@NotNull final SimulateShippingCostInput input) {
        final ShippingCalculator shipping = new ShippingCalculator(1000);
        input.items().forEach(item -> {
            final StockItem stockItem = repository.getById(item.idItem());
            final ShippedItem shippedItem = new ShippedItem(stockItem.heightCm(), stockItem.widthCm(), stockItem.lengthCm(), stockItem.weightKg());
            shipping.addShippedItem(shippedItem);
        });
        return new SimulateShippingCostOutput(shipping.calculateCost());
    }
}
