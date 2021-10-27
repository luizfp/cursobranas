package br.com.luizfp.cursobranas.application.query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface OrderDao {
    @NotNull
    GetOrderOutput getOrder(@NotNull final String orderCode);
    @NotNull
    List<GetOrderOutput> getOrders();
}
