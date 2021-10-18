package br.com.luizfp.cursobranas.infra.api;

import br.com.luizfp.cursobranas.application.dto.GetOrderOutput;
import br.com.luizfp.cursobranas.application.usecase.GetOrder;
import br.com.luizfp.cursobranas.application.usecase.GetOrdersList;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class OrderService {
    @NotNull
    private final OrderRepository orderRepository;

    public OrderService(@NotNull final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @NotNull
    public List<GetOrderOutput> getOrders() {
        return new GetOrdersList(orderRepository).execute();
    }

    @NotNull
    public GetOrderOutput getOrderById(@NotNull final Long orderId) {
        return new GetOrder(orderRepository).execute(orderId);
    }
}
