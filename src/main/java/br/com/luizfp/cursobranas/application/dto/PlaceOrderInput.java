package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record PlaceOrderInput(@NotNull String cpf,
                              @NotNull List<PlaceOrderItemInput> orderItemInput,
                              @Nullable String couponCode) {

    public PlaceOrderInput(@NotNull String cpf,
                           @NotNull List<PlaceOrderItemInput> orderItemInput) {
        this(cpf, orderItemInput, null);
    }
}
