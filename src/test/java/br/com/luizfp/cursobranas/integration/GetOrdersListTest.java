package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.query.GetOrdersList;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.application.usecase.ValidateCoupon;
import br.com.luizfp.cursobranas.infra.dao.OrderDaoDatabase;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.repository.database.CouponRepositoryDatabase;
import br.com.luizfp.cursobranas.infra.repository.database.OrderRepositoryDatabase;
import br.com.luizfp.cursobranas.infra.repository.database.StockItemRepositoryDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class GetOrdersListTest {
    private static PlaceOrder placeOrder;
    private static GetOrdersList getOrders;

    @BeforeAll
    static void beforeAll() {
        final var databaseConnection = new DatabaseConnectionAdapter();
        placeOrder = new PlaceOrder(new ValidateCoupon(new CouponRepositoryDatabase(databaseConnection)),
                                    new OrderRepositoryDatabase(databaseConnection),
                                    new StockItemRepositoryDatabase(databaseConnection));
        final var orderDao = new OrderDaoDatabase(databaseConnection);
        getOrders = new GetOrdersList(orderDao);
    }

    @Test
    void shouldReturnListOfOrders() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)));
        placeOrder.execute(input, OffsetDateTime.now());
        final List<GetOrderOutput> output = getOrders.execute();
        assertThat(output).isNotNull();
        assertThat(output.size()).isAtLeast(1);
    }
}
