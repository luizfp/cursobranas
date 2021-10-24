package br.com.luizfp.cursobranas.integration;

import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceTest {
    @Autowired
    private TestRestTemplate restTemplate;

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
    void shouldGetOrderByIdFromApiEndpoint() {
        final ResponseEntity<GetOrderOutput> responseEntity = restTemplate.getForEntity(
                "/v1/orders/1",
                GetOrderOutput.class);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}
