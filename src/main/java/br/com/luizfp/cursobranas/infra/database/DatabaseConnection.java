package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

public interface DatabaseConnection {

    @NotNull
    <T> T runInTransaction(@NotNull final DatabaseTransactionRunner<T> runner);

    @NotNull
    DatabaseResultRow one(@NotNull final String query,
                          @Nullable final Object... parameters);

    @NotNull
    Collection<DatabaseResultRow> many(@NotNull final String query,
                                       @Nullable final Object... parameters);

    @NotNull
    Optional<DatabaseResultRow> maybeOne(@NotNull final String query,
                                         @Nullable final Object... parameters);

    void none(@NotNull final String query,
              @Nullable final Object... parameters);

    int save(@NotNull final String query,
             @Nullable final Object... parameters);

    @NotNull
    DatabaseResultRow saveReturning(@NotNull final String query,
                                    @Nullable final Object... parameters);
}
