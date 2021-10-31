package br.com.luizfp.cursobranas.domain.service;

import org.jetbrains.annotations.NotNull;

public class StockCalculatorException extends RuntimeException {

    public StockCalculatorException(@NotNull final String message) {
        super(message);
    }
}
