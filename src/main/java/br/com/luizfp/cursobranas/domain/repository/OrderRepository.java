package br.com.luizfp.cursobranas.domain.repository;

import br.com.luizfp.cursobranas.domain.entity.Order;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OrderRepository {

    @NotNull
    Long save(@NotNull final Order order);

    @NotNull
    Order getById(@NotNull final Long orderId);

    @NotNull
    List<Order> getOrders();
}
