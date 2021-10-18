package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import br.com.luizfp.cursobranas.infra.database.TransactionException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseTransactionTest {

    @Test
    void shouldRollbackTransaction() {
        final DatabaseConnection connection = new DatabaseConnectionAdapter();
        assertThrows(TransactionException.class, () ->
                connection.runInTransaction(tx -> {
                    tx.query("create table public.test_transation (id integer)");
                    assertThat(isTableExists(connection)).isTrue();
                    throw new TransactionException("Broken transaction.");
                }));
        assertThat(isTableExists(connection)).isFalse();
    }

    private boolean isTableExists(@NotNull final DatabaseConnection connection) {
        final DatabaseResultRow row =
                connection.query("select to_regclass('public.test_transaction') is not null as table_exists;");
        return row.get("table_exists");
    }
}
