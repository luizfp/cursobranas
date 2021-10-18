package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.application.dto.OrderCode;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.google.common.truth.Truth.assertThat;

public class OrderCodeTest {

    @Test
    void shouldGenerateAnOrderCode() {
        final OffsetDateTime dateTime = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        final Long sequence = 42L;
        final OrderCode orderCode = new OrderCode(dateTime);
        assertThat(orderCode.getValue(sequence)).isEqualTo("202100000042");
    }
}
