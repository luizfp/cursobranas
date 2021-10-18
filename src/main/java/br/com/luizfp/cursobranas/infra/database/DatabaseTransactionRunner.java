package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DatabaseTransactionRunner<T> {
    @NotNull
    T run(@NotNull final DatabaseConnection db);
}
