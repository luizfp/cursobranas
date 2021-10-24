package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.query.GetOrder;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.query.OrderDao;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.infra.dao.OrderDaoDatabase;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.repository.database.OrderRepositoryDatabase;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class GetOrderTest {

    @Test
    void shouldReturnOrderById() {
        final Long orderId = 1L;
        final DatabaseConnection connection = new DatabaseConnectionAdapter();
        final OrderDao orderDao = new OrderDaoDatabase(connection);
        final GetOrder getOrder = new GetOrder(orderDao);
        final GetOrderOutput output = getOrder.execute(orderId);
        assertThat(output).isNotNull();
        assertThat(output.items()).hasSize(3);
    }
}
