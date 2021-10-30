package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.CancelOrderInput;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import org.jetbrains.annotations.NotNull;

public final class CancelOrder {
    @NotNull
    private final OrderRepository orderRepository;

    public CancelOrder(@NotNull final AbstractRepositoryFactory factory) {
        this.orderRepository = factory.createOrderRepository();
    }

    public void execute(@NotNull final CancelOrderInput input) {

    }
}
