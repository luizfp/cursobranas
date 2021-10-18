package br.com.luizfp.cursobranas.infra.api;

import br.com.luizfp.cursobranas.application.dto.GetOrderOutput;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
import br.com.luizfp.cursobranas.infra.repository.database.OrderRepositoryDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/orders")
public final class OrderResource {
    @NotNull
    private final OrderService orderService =
            new OrderService(new OrderRepositoryDatabase(new DatabaseConnectionAdapter()));

    @GetMapping
    public ResponseEntity<List<GetOrderOutput>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("{orderId}")
    public ResponseEntity<GetOrderOutput> getOrderById(@PathVariable("orderId") final Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }
}
