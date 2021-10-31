package br.com.luizfp.cursobranas.infra.repository.memory;

import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.entity.Order;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class OrderRepositoryMemory implements OrderRepository {
    @NotNull
    private final List<Order> orders = new ArrayList<>();

    @Override
    public long nextSequence() {
        return 0;
    }

    @NotNull
    @Override
    public Long save(@NotNull final Order order) {
        orders.add(order);
        return 1L;
    }

    @NotNull
    @Override
    public Order getById(@NotNull final Long orderId) {
        throw new IllegalStateException();
    }

    @Override
    public void update(@NotNull final Order order) {
        throw new IllegalStateException();
    }
}
