package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface DatabaseConnection {

    @NotNull <T> T runInTransaction(@NotNull final DatabaseTransactionRunner<T> runner);

    @NotNull <T> T query(@NotNull final String query,
                         @Nullable final Object... parameters);

    @NotNull
    <T> T one(@NotNull final String query,
              @Nullable final Object... parameters);

    @NotNull
    <T extends Collection<?>> T many(@NotNull final String query,
                                     @Nullable final Object... parameters);

    int save(@NotNull final String query,
             @Nullable final Object... parameters);

    @NotNull
    DatabaseResultRow saveReturning(@NotNull final String query,
                                    @Nullable final Object... parameters);
}
