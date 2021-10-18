package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

public record SimulateShippingCostItemInput(@NotNull Long idItem,
                                            int quantity) {

}
