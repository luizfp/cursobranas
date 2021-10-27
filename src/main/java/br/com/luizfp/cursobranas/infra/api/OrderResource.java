package br.com.luizfp.cursobranas.infra.api;

import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.infra.dao.OrderDaoDatabase;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnectionAdapter;
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
            new OrderService(new OrderDaoDatabase(new DatabaseConnectionAdapter()));

    @GetMapping
    public ResponseEntity<List<GetOrderOutput>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("{orderCode}")
    public ResponseEntity<GetOrderOutput> getOrderByCode(@PathVariable("orderCode") final String orderCode) {
        return ResponseEntity.ok(orderService.getOrderByCode(orderCode));
    }
}
