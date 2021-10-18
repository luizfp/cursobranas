package br.com.luizfp.cursobranas.infra.database;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record DatabaseResultRow(@NotNull Map<String, Object> columns) {

    public <T> T get(@NotNull final String columnName) {
        //noinspection unchecked
        return (T) columns.get(columnName);
    }
}
