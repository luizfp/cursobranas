package br.com.luizfp.cursobranas.infra.api;

import br.com.luizfp.cursobranas.application.query.GetOrder;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.query.GetOrdersList;
import br.com.luizfp.cursobranas.application.query.OrderDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class OrderService {
    @NotNull
    private final OrderDao orderDao;

    public OrderService(@NotNull final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @NotNull
    public List<GetOrderOutput> getOrders() {
        return new GetOrdersList(orderDao).execute();
    }

    @NotNull
    public GetOrderOutput getOrderByCode(@NotNull final String orderCode) {
        return new GetOrder(orderDao).execute(orderCode);
    }
}
