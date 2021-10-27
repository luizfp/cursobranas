package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.OrderCode;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.google.common.truth.Truth.assertThat;

public class OrderCodeTest {

    @Test
    void shouldGenerateAnOrderCode() {
        final OffsetDateTime dateTime = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        final long sequence = 42;
        final OrderCode orderCode = new OrderCode(dateTime, sequence);
        assertThat(orderCode.getValue()).isEqualTo("202100000042");
    }
}
