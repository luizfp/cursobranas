package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public final class DatabaseConnectionAdapter implements DatabaseConnection {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/cursobranas";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";
    @NotNull
    private final InternalDatabaseConnectionAdapter internal;

    public DatabaseConnectionAdapter() {
        this.internal = new InternalDatabaseConnectionAdapter();
    }

    @NotNull
    @Override
    public <T> T runInTransaction(@NotNull final DatabaseTransactionRunner<T> runner) {
        try (final Connection conn = createConnection(); final Transaction tx = new Transaction(conn)) {
            final T response = runner.run(tx);
            tx.commit();
            return response;
        } catch (final Throwable t) {
            throw new TransactionException("Error to run database transaction.", t);
        }
    }

    @NotNull
    @Override
    public DatabaseResultRow one(@NotNull final String query,
                                 @Nullable final Object... parameters) {
        return internal.internalQuery(createConnection(), query, QueryType.ONE, parameters);
    }

    @NotNull
    @Override
    public Collection<DatabaseResultRow> many(@NotNull final String query,
                                              @Nullable final Object... parameters) {
        return internal.internalQuery(createConnection(), query, QueryType.MANY, parameters);
    }

    @NotNull
    @Override
    public Optional<DatabaseResultRow> maybeOne(@NotNull final String query,
                                                @Nullable final Object... parameters) {
        return internal.internalQuery(createConnection(), query, QueryType.MAYBE_ONE, parameters);
    }

    @Override
    public void none(@NotNull final String query,
                     @Nullable final Object... parameters) {
        internal.internalQuery(createConnection(), query, QueryType.NONE, parameters);
    }

    @Override
    public int save(@NotNull final String query,
                    @Nullable final Object... parameters) {
        return internal.internalSave(createConnection(), query, parameters);
    }

    @NotNull
    @Override
    public DatabaseResultRow saveReturning(@NotNull final String query,
                                           @Nullable final Object... parameters) {
        return internal.internalSaveReturning(createConnection(), query, parameters);
    }

    @NotNull
    private Connection createConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (final SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
