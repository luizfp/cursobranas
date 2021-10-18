package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.usecase.GetOrdersList;
import br.com.luizfp.cursobranas.application.dto.GetOrderOutput;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.repository.database.OrderRepositoryDatabase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class GetOrdersListTest {

    @Test
    void shouldReturnListOfOrders() {
        final DatabaseConnection connection = new DatabaseConnectionAdapter();
        final OrderRepository orderRepository = new OrderRepositoryDatabase(connection);
        final GetOrdersList getOrders = new GetOrdersList(orderRepository);
        final List<GetOrderOutput> output = getOrders.execute();
        assertThat(output).isNotNull();
        assertThat(output.size()).isAtLeast(1);
    }
}
