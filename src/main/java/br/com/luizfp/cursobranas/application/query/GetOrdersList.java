package br.com.luizfp.cursobranas.application.query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class GetOrdersList {

    @NotNull
    private final OrderDao orderDao;

    public GetOrdersList(@NotNull final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @NotNull
    public List<GetOrderOutput> execute() {
        return orderDao.getOrders();
    }
}
