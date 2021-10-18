package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostInput;
import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostItemInput;
import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostOutput;
import br.com.luizfp.cursobranas.application.usecase.SimulateShippingCost;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.repository.database.StockItemRepositoryDatabase;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SimulateShippingCostTest {

    @Test
    void shouldSimulateShippingCost() {
        final DatabaseConnectionAdapter databaseConnection = new DatabaseConnectionAdapter();
        final StockItemRepository repository = new StockItemRepositoryDatabase(databaseConnection);
        final SimulateShippingCostInput input = new SimulateShippingCostInput(
                List.of(new SimulateShippingCostItemInput(1L, 1),
                        new SimulateShippingCostItemInput(2L, 1),
                        new SimulateShippingCostItemInput(3L, 4)));
        final SimulateShippingCost simulator = new SimulateShippingCost(repository);
        final SimulateShippingCostOutput output = simulator.execute(input);
        Truth.assertThat(output.totalShippingCost()).isEqualTo(11);
    }
}
