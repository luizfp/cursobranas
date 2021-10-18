package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DatabaseConnection {

    @NotNull <T> T runInTransaction(@NotNull final DatabaseTransactionRunner<T> runner);

    @NotNull
    <T> T query(@NotNull final String query,
                @Nullable final Object... parameters);

    int save(@NotNull final String query,
             @Nullable final Object... parameters);

    @NotNull
    DatabaseResultRow saveReturning(@NotNull final String query,
                                    @Nullable final Object... parameters);
}
