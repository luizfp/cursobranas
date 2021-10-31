package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.domain.entity.CouponNotFoundException;
import br.com.luizfp.cursobranas.domain.entity.OutOfStockException;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.factory.DatabaseRepositoryFactory;
import org.junit.jupiter.api.*;

import java.time.OffsetDateTime;
import java.util.List;

import static br.com.luizfp.cursobranas.domain.entity.StockEntryOperation.IN;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlaceOrderTest {
    private static final DatabaseConnection databaseConnection = new DatabaseConnectionAdapter();
    private AbstractRepositoryFactory repositoryFactory;

    @BeforeAll
    static void beforeAll() {
        databaseConnection.none("""
                                        insert into coupon (code, expires_at, percentage_discount, active)
                                        values ('10OFF', '2021-10-10T10:00:00', 0.1, true)
                                        """);
    }

    @AfterAll
    static void afterAll() {
        databaseConnection.none("""
                                        update coupon set active = false where code = '10OFF' and active = true
                                        """);
    }

    @BeforeEach
    void beforeEach() {
        repositoryFactory = new DatabaseRepositoryFactory(databaseConnection);
    }

    @AfterEach
    void afterEach() {
        repositoryFactory.createStockEntryRepository().clean();
    }

    @Test
    void shouldPlaceAnOrder() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)));
        final PlaceOrder placeOrder = new PlaceOrder(repositoryFactory);
        final OffsetDateTime orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        final PlaceOrderOutput output = placeOrder.execute(input, orderCreatedAt);
        assertThat(output.orderId()).isGreaterThan(0);
        assertThat(output.orderTotal()).isEqualTo(1450);
    }

    @Test
    void shouldPlaceAnOrderWith10OffCoupon() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)),
                "10OFF");
        final PlaceOrder placeOrder = new PlaceOrder(repositoryFactory);
        final OffsetDateTime orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        final PlaceOrderOutput output = placeOrder.execute(input, orderCreatedAt);
        assertThat(output.orderId()).isGreaterThan(0);
        assertThat(output.orderTotal()).isEqualTo(1305);
    }

    @Test
    void shouldThrowsWithInvalidCoupon() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)),
                "NOT_FOUND");
        final PlaceOrder placeOrder = new PlaceOrder(repositoryFactory);
        final OffsetDateTime orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        assertThrows(CouponNotFoundException.class, () -> placeOrder.execute(input, orderCreatedAt));
    }

    @Test
    void shouldThrowsInsufficientStockItems() {
        final var input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 20)));
        final var placeOrder = new PlaceOrder(repositoryFactory);
        final var orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        assertThrows(OutOfStockException.class, () -> placeOrder.execute(input, orderCreatedAt));
    }

    @Test
    void shouldDebitStockAfterPlaceOrder() {
        final var input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 10)));
        final var placeOrder = new PlaceOrder(repositoryFactory);
        final var orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        placeOrder.execute(input, orderCreatedAt);
        assertThat(getQuantityAvailableForStockItem(1)).isEqualTo(0);
    }

    private int getQuantityAvailableForStockItem(final long itemId) {
        return repositoryFactory
                .createStockEntryRepository()
                .getByItemId(itemId)
                .stream()
                .mapToInt(entry -> entry.operation() == IN ? entry.quantity() : -entry.quantity())
                .sum();
    }
}
