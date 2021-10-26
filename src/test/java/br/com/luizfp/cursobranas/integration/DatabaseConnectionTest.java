package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class DatabaseConnectionTest {

    @Test
    void shouldCreateDatabaseConnection() {
        final DatabaseConnection databaseConnection = new DatabaseConnectionAdapter();
        final DatabaseResultRow result = databaseConnection.one("select 1");
        assertThat(result).isNotNull();
    }
}
