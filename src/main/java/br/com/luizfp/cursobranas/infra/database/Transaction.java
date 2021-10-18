package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;

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
    public <T> T query(@NotNull final String query,
                       @Nullable final Object... parameters) {
        return internal.internalQuery(connection, query, parameters);
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
