package br.com.luizfp.cursobranas.unit;

import br.com.luizfp.cursobranas.domain.entity.Cpf;
import br.com.luizfp.cursobranas.domain.entity.InvalidCpfException;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CpfTest {

    @Test
    void shouldThrowsWithInvalidCpf() {
        assertThrows(InvalidCpfException.class, () -> new Cpf("123.456.789-10"));
    }

    @Test
    void shouldFailWithCpfWithAllDigitsEqual() {
        assertThrows(InvalidCpfException.class, () -> new Cpf("111.111.111-11"));
    }

    @Test
    void shouldFailWithSmallCpf() {
        assertThrows(InvalidCpfException.class, () -> new Cpf("111.111"));
    }

    @Test
    void shouldFailWithLargeCpf() {
        assertThrows(InvalidCpfException.class, () -> new Cpf("111.111.111.111-11"));
    }

    @Test
    void shouldValidateCpfWithoutSeparators() {
        assertThat(new Cpf("58487625975")).isNotNull();
    }

    @Test
    void shouldValidateCpfWithSeparators() {
        assertThat(new Cpf("584.876.259-75")).isNotNull();
    }
}
