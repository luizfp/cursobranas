package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SimulateShippingCostInput(@NotNull List<SimulateShippingCostItemInput> items) {

}
