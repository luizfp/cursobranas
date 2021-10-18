package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.*;

public final class InternalDatabaseConnectionAdapter {

    @SuppressWarnings("unchecked")
    @NotNull
    public <T> T internalQuery(@NotNull final Connection connection,
                               @NotNull final String query,
                               @Nullable final Object... parameters) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = createStatement(connection, query, parameters);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                final List<DatabaseResultRow> lines = new ArrayList<>();
                do {
                    lines.add(createDatabaseLineItem(resultSet));
                } while (resultSet.next());
                return lines.size() == 1 ? (T) lines.get(0) : (T) lines;
            } else {
                return (T) Collections.emptyList();
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement, resultSet);
        }
    }

    public int internalSave(@NotNull final Connection connection,
                            @NotNull final String query,
                            @Nullable final Object... parameters) {
        PreparedStatement statement = null;
        try {
            statement = createStatement(connection, query, parameters);
            return statement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement);
        }
    }

    @NotNull
    public DatabaseResultRow internalSaveReturning(@NotNull final Connection connection,
                                                   @NotNull final String query,
                                                   @Nullable final Object... parameters) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = createStatement(connection, query, parameters);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createDatabaseLineItem(resultSet);
            } else {
                throw new IllegalArgumentException("No data returned from query '%s'.".formatted(query));
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(statement, resultSet);
        }
    }

    @NotNull
    private DatabaseResultRow createDatabaseLineItem(@NotNull final ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        final Map<String, Object> columns = new HashMap<>(columnCount);
        for (int i = 1; i <= columnCount; ++i) {
            final String columnTypeName = metaData.getColumnTypeName(i);
            if (columnTypeName.equals("timestamptz")) {
                columns.put(metaData.getColumnName(i), resultSet.getObject(i, OffsetDateTime.class));
            } else {
                columns.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
        }
        return new DatabaseResultRow(columns);
    }

    @NotNull
    private PreparedStatement createStatement(@NotNull final Connection connection,
                                              @NotNull final String query,
                                              @Nullable final Object[] parameters) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(query);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
        }
        return statement;
    }

    private void close(@Nullable final AutoCloseable... closeables) {
        Arrays.stream(closeables)
                .filter(Objects::nonNull)
                .forEach(closeable -> {
                    try {
                        closeable.close();
                    } catch (final Exception ignore) {
                    }
                });
    }
}
