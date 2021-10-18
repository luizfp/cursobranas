package br.com.luizfp.cursobranas.application.dto;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public final class OrderCode {
    @NotNull
    private final OffsetDateTime dateTime;

    public OrderCode(@NotNull final OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @NotNull
    public String getValue(@NotNull final Long orderSequence) {
        return "%d%08d".formatted(dateTime.getYear(), orderSequence);
    }
}
