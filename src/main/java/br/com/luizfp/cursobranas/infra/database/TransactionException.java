package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;

public class TransactionException extends RuntimeException {

    public TransactionException(@NotNull final String message) {
        super(message);
    }

    public TransactionException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
