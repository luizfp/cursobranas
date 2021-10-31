package br.com.luizfp.cursobranas.infra.repository.database;

import br.com.luizfp.cursobranas.domain.entity.*;
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
                                                                          sequence,
                                                                          user_cpf,
                                                                          used_coupon_id,
                                                                          created_at,
                                                                          order_total,
                                                                          shipping_cost)
                                                                          values (?, ?, ?, ?, ?, ?, ?) returning id;
                                                                          """,
                                                                  order.getOrderCode(),
                                                                  order.getSequence(),
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

    @NotNull
    @Override
    public Order getById(@NotNull final Long orderId) {
        final var db = databaseConnection.one("select * from orders o where o.id = ?", orderId);
        final int sequence = db.get("sequence");
        final var order = new Order(db.get("user_cpf"),
                                    db.get("created_at"),
                                    sequence,
                                    OrderStatus.valueOf(db.get("status")));
        final var dbItems = databaseConnection.many("""
                                                            select * from order_item oi
                                                            join stock_item si on oi.item_id = si.id
                                                            where oi.order_id = ?
                                                            """, orderId);
        dbItems.forEach(dbItem -> order.addItem(new StockItem(dbItem.get("id"),
                                                              dbItem.get("category"),
                                                              dbItem.get("description"),
                                                              dbItem.get("price"),
                                                              dbItem.get("height_cm"),
                                                              dbItem.get("width_cm"),
                                                              dbItem.get("length_cm"),
                                                              dbItem.get("weight_kg")),
                                                dbItem.get("quantity")));
        final Long usedCouponId = db.get("used_coupon_id");
        if (usedCouponId != null) {
            final var dbCoupon = databaseConnection.one("select * from coupon c where c.id = ?", usedCouponId);
            order.applyCoupon(new Coupon(dbCoupon.get("id"),
                                         dbCoupon.get("code"),
                                         dbCoupon.get("expires_at"),
                                         dbCoupon.get("percentage_discount")));
        }
        return order;
    }

    @Override
    public void update(@NotNull final Order order) {
        databaseConnection
                .runInTransaction(tx -> {
                    tx.none("update orders set status = ? where code = ?",
                            order.getStatus().toString(),
                            order.getOrderCode());
                    if (order.isCancelled()) {
                        order.getOrderItems().forEach(item -> {
                            tx.none("insert into stock_entry (item_id, operation, quantity) values (?, 'IN', ?)",
                                    item.id(),
                                    item.quantity());
                        });
                    }
                    return Void.TYPE;
                });
    }
}
