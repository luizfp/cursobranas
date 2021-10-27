package br.com.luizfp.cursobranas.domain.repository;

import br.com.luizfp.cursobranas.domain.entity.Order;
import org.jetbrains.annotations.NotNull;

public interface OrderRepository {
    long nextSequence();

    @NotNull
    Long save(@NotNull final Order order);
}
