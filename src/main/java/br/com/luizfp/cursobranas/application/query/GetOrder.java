package br.com.luizfp.cursobranas.application.query;

import org.jetbrains.annotations.NotNull;

public final class GetOrder {
    @NotNull
    private final OrderDao orderDao;

    public GetOrder(@NotNull final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @NotNull
    public GetOrderOutput execute(@NotNull final Long orderId) {
        return orderDao.getOrder(orderId);
    }
}
