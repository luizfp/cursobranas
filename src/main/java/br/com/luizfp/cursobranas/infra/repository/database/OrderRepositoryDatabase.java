package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.OrderItem;
import br.com.luizfp.cursobranas.domain.entity.StockItem;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class OrderRepositoryDatabase implements OrderRepository {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public OrderRepositoryDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public Long save(@NotNull final Order order) {
        return databaseConnection
                .runInTransaction(tx -> {
                    final DatabaseResultRow db = tx.saveReturning("""
                                                                          insert into orders (
                                                                          user_cpf, 
                                                                          used_coupon_id, 
                                                                          created_at, 
                                                                          order_total, 
                                                                          shipping_cost) 
                                                                          values (?, ?, ?, ?, ?) returning id;
                                                                          """,
                                                                  order.getCpf(),
                                                                  order.getCouponIdOrNull(),
                                                                  order.getCreatedAt(),
                                                                  order.calculateOrderTotal(),
                                                                  order.calculateShippingCost(1000));
                    final Long orderId = db.get("id");
                    for (final OrderItem item : order.getOrderItems()) {
                        tx.save("""
                                        insert into order_item (order_id,
                                                                category,
                                                                description,
                                                                price,
                                                                height_cm,
                                                                width_cm,
                                                                length_cm,
                                                                weight_kg,
                                                                quantity)
                                        values (?, ?, ?, ?, ?, ?, ?, ?, ?);
                                        """,
                                orderId,
                                item.category(),
                                item.description(),
                                item.price(),
                                item.heightCm(),
                                item.widthCm(),
                                item.lengthCm(),
                                item.weightKg(),
                                item.quantity());
                    }
                    return orderId;
                });
    }

    @NotNull
    @Override
    public Order getById(@NotNull final Long orderId) {
        return databaseConnection
                .runInTransaction(tx -> {
                    final DatabaseResultRow dbOrder = tx.query("select * from orders o where o.id = ?", orderId);
                    final Order order = new Order(dbOrder.get("user_cpf"), dbOrder.get("created_at"));
                    createOrderItems(tx, orderId, order);
                    return order;
                });
    }

    @NotNull
    @Override
    public List<Order> getOrders() {
        return databaseConnection
                .runInTransaction(tx -> {
                    final List<DatabaseResultRow> dbOrders = tx.query("select * from orders");
                    final List<Order> orders = new ArrayList<>();
                    for (final DatabaseResultRow dbOrder : dbOrders) {
                        final Order order = new Order(dbOrder.get("user_cpf"), dbOrder.get("created_at"));
                        final Long orderId = dbOrder.get("id");
                        createOrderItems(tx, orderId, order);
                        orders.add(order);
                    }
                    return orders;
                });
    }

    private void createOrderItems(@NotNull final DatabaseConnection tx,
                                  @NotNull final Long orderId,
                                  @NotNull final Order order) {
        final List<DatabaseResultRow> dbItems =
                tx.query("select * from order_item oi where oi.order_id = ?", orderId);
        dbItems.forEach(dbItem -> order.addItem(
                new StockItem(dbItem.get("id"),
                              dbItem.get("category"),
                              dbItem.get("description"),
                              dbItem.get("price"),
                              dbItem.get("height_cm"),
                              dbItem.get("width_cm"),
                              dbItem.get("length_cm"),
                              dbItem.get("weight_kg")),
                dbItem.get("quantity")));
    }
}
