package br.com.luizfp.cursobranas.domain.entity;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public final class OrderCode {
    @NotNull
    private final String value;

    public OrderCode(@NotNull final OffsetDateTime dateTime,
                     final long sequence) {
        this.value = generateValue(dateTime, sequence);
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @NotNull
    private String generateValue(@NotNull final OffsetDateTime dateTime,
                                 final long sequence) {
        return "%d%08d".formatted(dateTime.getYear(), sequence);
    }
}
