package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.CancelOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.query.GetOrder;
import br.com.luizfp.cursobranas.application.usecase.CancelOrder;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.infra.dao.OrderDaoDatabase;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.factory.DatabaseRepositoryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

public class CancelOrderTest {
    private static DatabaseRepositoryFactory repositoryFactory;
    private static PlaceOrder placeOrder;
    private static GetOrder getOrder;

    @BeforeAll
    static void beforeAll() {
        final var databaseConnection = new DatabaseConnectionAdapter();
        repositoryFactory = new DatabaseRepositoryFactory(databaseConnection);
        placeOrder = new PlaceOrder(repositoryFactory);
        final var orderDao = new OrderDaoDatabase(databaseConnection);
        getOrder = new GetOrder(orderDao);
    }

    @Test
    void shouldCancelAnOrder() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5)));
        final var orderOutput = placeOrder.execute(input, OffsetDateTime.now());
        final var cancelOrderInput = new CancelOrderInput(orderOutput.orderId());
        final var cancelOrder = new CancelOrder(repositoryFactory);
        Assertions.assertDoesNotThrow(() -> cancelOrder.execute(cancelOrderInput));
    }
}