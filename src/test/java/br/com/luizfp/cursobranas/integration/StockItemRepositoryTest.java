package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.domain.entity.StockItemNotFoundException;
import br.com.luizfp.cursobranas.infra.repository.memory.StockItemRepositoryMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StockItemRepositoryTest {

    @Test
    void shouldThrowsStockItemNotFoundException() {
        final StockItemRepositoryMemory repository = new StockItemRepositoryMemory();
        Assertions.assertThrows(StockItemNotFoundException.class, () -> repository.getById(-1L));
    }
}
