package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.GetOrderItemOutput;
import br.com.luizfp.cursobranas.application.dto.GetOrderOutput;
import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.OrderItem;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GetOrder {
    @NotNull
    private final OrderRepository orderRepository;

    public GetOrder(@NotNull final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @NotNull
    public GetOrderOutput execute(@NotNull final Long orderId) {
        final Order order = orderRepository.getById(orderId);
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
        return new GetOrderOutput(orderId, order.generateOrderCode(orderId), items);
    }
}
