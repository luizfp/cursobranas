package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.OrderItem;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

public final class OrderRepositoryDatabase implements OrderRepository {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public OrderRepositoryDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public long nextSequence() {
        final DatabaseResultRow row = databaseConnection.one("select nextval('order_code_seq') as sequence");
        return row.get("sequence");
    }

    @NotNull
    @Override
    public Long save(@NotNull final Order order) {
        return databaseConnection
                .runInTransaction(tx -> {
                    final DatabaseResultRow db = tx.saveReturning("""
                                                                          insert into orders (
                                                                          code,
                                                                          user_cpf,
                                                                          used_coupon_id,
                                                                          created_at,
                                                                          order_total,
                                                                          shipping_cost)
                                                                          values (?, ?, ?, ?, ?, ?) returning id;
                                                                          """,
                                                                  order.getOrderCode(),
                                                                  order.getCpf(),
                                                                  order.getCouponIdOrNull(),
                                                                  order.getCreatedAt(),
                                                                  order.calculateOrderTotal(),
                                                                  order.calculateShippingCost(1000));
                    final Long orderId = db.get("id");
                    for (final OrderItem item : order.getOrderItems()) {
                        tx.save("""
                                        insert into order_item (order_id,
                                                                item_id,
                                                                price,
                                                                quantity)
                                        values (?, ?, ?, ?);
                                        """,
                                orderId,
                                item.id(),
                                item.price(),
                                item.quantity());
                        tx.none("insert into stock_entry (item_id, operation, quantity) values (?, 'OUT', ?)",
                                item.id(),
                                item.quantity());
                    }
                    return orderId;
                });
    }
}
