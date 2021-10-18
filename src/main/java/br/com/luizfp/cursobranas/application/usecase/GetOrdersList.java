package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.GetOrderItemOutput;
import br.com.luizfp.cursobranas.application.dto.GetOrderOutput;
import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.OrderItem;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GetOrdersList {

    @NotNull
    private final OrderRepository orderRepository;

    public GetOrdersList(@NotNull final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @NotNull
    public List<GetOrderOutput> execute() {
        final List<Order> orders = orderRepository.getOrders();
        final List<GetOrderOutput> output = new ArrayList<>();
        for (final Order order : orders) {
            final List<GetOrderItemOutput> items = new ArrayList<>();
            for (final OrderItem item : order.getOrderItems()) {
                items.add(new GetOrderItemOutput(item.id(),
                                                 item.category(),
                                                 item.description(),
                                                 item.price(),
                                                 item.heightCm(),
                                                 item.widthCm(),
                                                 item.lengthCm(),
                                                 item.weightKg(),
                                                 item.quantity()));
            }
            // TODO:
            final GetOrderOutput orderOutput = new GetOrderOutput(1L, order.generateOrderCode(1L), items);
            output.add(orderOutput);
        }
        return output;
    }
}
