package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.application.usecase.ValidateCoupon;
import br.com.luizfp.cursobranas.domain.entity.CouponNotFoundException;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.repository.database.OrderRepositoryDatabase;
import br.com.luizfp.cursobranas.infra.repository.database.StockItemRepositoryDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlaceOrderTest {
    @Mock
    CouponRepository couponRepository;

    @Test
    void shouldPlaceAnOrder() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)));
        final DatabaseConnectionAdapter databaseConnection = new DatabaseConnectionAdapter();
        final PlaceOrder placeOrder = new PlaceOrder(new ValidateCoupon(couponRepository),
                                                     new OrderRepositoryDatabase(databaseConnection),
                                                     new StockItemRepositoryDatabase(databaseConnection));
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
        final DatabaseConnectionAdapter databaseConnection = new DatabaseConnectionAdapter();
        final PlaceOrder placeOrder = new PlaceOrder(new ValidateCoupon(couponRepository),
                                                     new OrderRepositoryDatabase(databaseConnection),
                                                     new StockItemRepositoryDatabase(databaseConnection));
        final OffsetDateTime orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        Mockito.when(couponRepository.getByCode("NOT_FOUND")).thenReturn(Optional.empty());
        assertThrows(CouponNotFoundException.class, () -> placeOrder.execute(input, orderCreatedAt));
    }

    @Test
    void shouldGenerateAnOrderCode() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75", List.of(new PlaceOrderItemInput(1, 5)));
        final ValidateCoupon validateCoupon = new ValidateCoupon(couponRepository);
        final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(42L);
        final StockItemRepositoryDatabase stockItemRepository = new StockItemRepositoryDatabase(new DatabaseConnectionAdapter());
        final PlaceOrder placeOrder = new PlaceOrder(validateCoupon, orderRepository, stockItemRepository);
        final OffsetDateTime orderCreatedAt = OffsetDateTime.parse("2021-01-01T10:00:00+00:00");
        final PlaceOrderOutput output = placeOrder.execute(input, orderCreatedAt);
        assertThat(output.orderCode()).isEqualTo("202100000042");
    }
}
