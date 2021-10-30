package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostInput;
import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostItemInput;
import br.com.luizfp.cursobranas.application.dto.SimulateShippingCostOutput;
import br.com.luizfp.cursobranas.application.usecase.SimulateShippingCost;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.factory.DatabaseRepositoryFactory;
import com.google.common.truth.Truth;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SimulateShippingCostTest {

    @Test
    void shouldSimulateShippingCost() {
        final SimulateShippingCostInput input = new SimulateShippingCostInput(
                List.of(new SimulateShippingCostItemInput(1L, 1),
                        new SimulateShippingCostItemInput(2L, 1),
                        new SimulateShippingCostItemInput(3L, 4)));
        final var databaseConnection = new DatabaseConnectionAdapter();
        final var repositoryFactory = new DatabaseRepositoryFactory(databaseConnection);
        final SimulateShippingCost simulator = new SimulateShippingCost(repositoryFactory);
        final SimulateShippingCostOutput output = simulator.execute(input);
        Truth.assertThat(output.totalShippingCost()).isEqualTo(11);
    }
}
