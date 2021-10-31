package br.com.luizfp.cursobranas.application.query;

import br.com.luizfp.cursobranas.domain.entity.OrderStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final record GetOrderOutput(@NotNull Long orderId,
                                   @NotNull String orderCode,
                                   @NotNull OrderStatus orderStatus,
                                   @NotNull List<GetOrderItemOutput> items) {
}
