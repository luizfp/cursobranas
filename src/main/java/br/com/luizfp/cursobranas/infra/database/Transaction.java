package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public final class Transaction implements DatabaseConnection, AutoCloseable {
    @NotNull
    private final Connection connection;
    @NotNull
    private final InternalDatabaseConnectionAdapter internal;
    private boolean committed;

    public Transaction(@NotNull final Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
        this.internal = new InternalDatabaseConnectionAdapter();
    }

    @NotNull
    @Override
    public <T> T runInTransaction(@NotNull final DatabaseTransactionRunner<T> runner) {
        // We are already inside a transaction.
        return runner.run(this);
    }

    @NotNull
    @Override
    public DatabaseResultRow one(@NotNull final String query,
                                 @Nullable final Object... parameters) {
        return internal.internalQuery(connection, query, QueryType.ONE, parameters);
    }

    @NotNull
    @Override
    public Collection<DatabaseResultRow> many(@NotNull final String query,
                                              @Nullable final Object... parameters) {
        return internal.internalQuery(connection, query, QueryType.MANY, parameters);
    }

    @NotNull
    @Override
    public Optional<DatabaseResultRow> maybeOne(@NotNull final String query,
                                                @Nullable final Object... parameters) {
        return internal.internalQuery(connection, query, QueryType.MAYBE_ONE, parameters);
    }

    @Override
    public void none(@NotNull final String query,
                     @Nullable final Object... parameters) {
        internal.internalQuery(connection, query, QueryType.NONE, parameters);
    }

    @Override
    public int save(@NotNull final String query,
                    @Nullable final Object... parameters) {
        return internal.internalSave(connection, query, parameters);
    }

    @NotNull
    @Override
    public DatabaseResultRow saveReturning(@NotNull final String query,
                                           @Nullable final Object... parameters) {
        return internal.internalSaveReturning(connection, query, parameters);
    }

    @Override
    public void close() throws SQLException {
        if (!committed) {
            connection.rollback();
        }
        connection.close();
    }

    public void commit() throws SQLException {
        connection.commit();
        committed = true;
    }
}
