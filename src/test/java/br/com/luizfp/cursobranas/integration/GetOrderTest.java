package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.application.query.GetOrder;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.infra.dao.OrderDaoDatabase;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.factory.DatabaseRepositoryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class GetOrderTest {
    private static DatabaseConnectionAdapter databaseConnection;
    private static PlaceOrder placeOrder;
    private static GetOrder getOrder;

    @BeforeAll
    static void beforeAll() {
        databaseConnection = new DatabaseConnectionAdapter();
        final var repositoryFactory = new DatabaseRepositoryFactory(databaseConnection);
        placeOrder = new PlaceOrder(repositoryFactory);
        final var orderDao = new OrderDaoDatabase(databaseConnection);
        getOrder = new GetOrder(orderDao);
    }

    @AfterEach
    void afterEach() {
        databaseConnection.none("update stock_item set quantity_available = 10");
    }

    @Test
    void shouldReturnListOfOrders() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)));
        final PlaceOrderOutput placedOrder = placeOrder.execute(input, OffsetDateTime.now());
        final GetOrderOutput output = getOrder.execute(placedOrder.orderCode());
        assertThat(output).isNotNull();
        assertThat(output.orderId()).isEqualTo(placedOrder.orderId());
    }
}
