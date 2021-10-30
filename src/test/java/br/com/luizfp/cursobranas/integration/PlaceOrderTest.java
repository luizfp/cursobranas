package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.domain.entity.CouponNotFoundException;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.factory.DatabaseRepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlaceOrderTest {
    private AbstractRepositoryFactory repositoryFactory;

    @BeforeEach
    void beforeEach() {
        final var databaseConnection = new DatabaseConnectionAdapter();
        repositoryFactory = new DatabaseRepositoryFactory(databaseConnection);
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
}
