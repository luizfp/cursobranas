package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.query.GetOrdersList;
import br.com.luizfp.cursobranas.application.query.OrderDao;
import br.com.luizfp.cursobranas.infra.dao.OrderDaoDatabase;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class GetOrdersListTest {

    @Test
    void shouldReturnListOfOrders() {
        final DatabaseConnection connection = new DatabaseConnectionAdapter();
        final OrderDao orderDao = new OrderDaoDatabase(connection);
        final GetOrdersList getOrders = new GetOrdersList(orderDao);
        final List<GetOrderOutput> output = getOrders.execute();
        assertThat(output).isNotNull();
        assertThat(output.size()).isAtLeast(1);
    }
}
