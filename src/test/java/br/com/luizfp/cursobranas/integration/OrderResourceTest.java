package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderItemInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.usecase.PlaceOrder;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.factory.DatabaseRepositoryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceTest {
    private static DatabaseConnectionAdapter databaseConnection;
    @Autowired
    private TestRestTemplate restTemplate;
    private static PlaceOrder placeOrder;

    @BeforeAll
    static void beforeAll() {
        databaseConnection = new DatabaseConnectionAdapter();
        final var repositoryFactory = new DatabaseRepositoryFactory(databaseConnection);
        placeOrder = new PlaceOrder(repositoryFactory);
    }

    @AfterEach
    void afterEach() {
        databaseConnection.none("update stock_item set quantity_available = 10");
    }

    @Test
    void shouldListOrdersFromApiEndpoint() {
        final ResponseEntity<List<GetOrderOutput>> responseEntity = restTemplate.exchange(
                "/v1/orders/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void shouldGetOrderByCodeFromApiEndpoint() {
        final PlaceOrderInput input = new PlaceOrderInput(
                "584.876.259-75",
                List.of(new PlaceOrderItemInput(1, 5),
                        new PlaceOrderItemInput(2, 2),
                        new PlaceOrderItemInput(3, 1)));
        final PlaceOrderOutput output = placeOrder.execute(input, OffsetDateTime.now());
        final ResponseEntity<GetOrderOutput> responseEntity = restTemplate.getForEntity(
                "/v1/orders/%s".formatted(output.orderCode()),
                GetOrderOutput.class);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}
