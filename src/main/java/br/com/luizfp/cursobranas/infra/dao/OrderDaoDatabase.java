package br.com.luizfp.cursobranas.infra.dao;

import br.com.luizfp.cursobranas.application.query.GetOrderItemOutput;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.query.OrderDao;
import br.com.luizfp.cursobranas.domain.entity.OrderStatus;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class OrderDaoDatabase implements OrderDao {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public OrderDaoDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public GetOrderOutput getOrder(@NotNull final String orderCode) {
        return databaseConnection
                .runInTransaction(tx -> {
                    final DatabaseResultRow dbOrder = tx.one("select * from orders o where o.code = ?", orderCode);
                    final Long orderId = dbOrder.get("id");
                    final var items = createOrderItems(tx, orderId);
                    return new GetOrderOutput(orderId, orderCode, OrderStatus.valueOf(dbOrder.get("status")), items);
                });
    }

    @NotNull
    @Override
    public List<GetOrderOutput> getOrders() {
        return databaseConnection
                .runInTransaction(tx -> {
                    final Collection<DatabaseResultRow> dbOrders = tx.many("select * from orders");
                    final List<GetOrderOutput> orders = new ArrayList<>();
                    for (final DatabaseResultRow dbOrder : dbOrders) {
                        final Long orderId = dbOrder.get("id");
                        final var items = createOrderItems(tx, orderId);
                        final GetOrderOutput order = new GetOrderOutput(orderId,
                                                                        dbOrder.get("code"),
                                                                        OrderStatus.valueOf(dbOrder.get("status")),
                                                                        items);
                        orders.add(order);
                    }
                    return orders;
                });
    }

    @NotNull
    private List<GetOrderItemOutput> createOrderItems(@NotNull final DatabaseConnection tx,
                                                      @NotNull final Long orderId) {
        final Collection<DatabaseResultRow> dbItems = tx.many("""
                                                                      select * from order_item oi
                                                                      join stock_item si on oi.item_id = si.id
                                                                      where oi.order_id = ?
                                                                      """, orderId);
        final List<GetOrderItemOutput> items = new ArrayList<>();
        dbItems.forEach(dbItem -> items.add(new GetOrderItemOutput(dbItem.get("id"),
                                                                   dbItem.get("category"),
                                                                   dbItem.get("description"),
                                                                   dbItem.get("price"),
                                                                   dbItem.get("height_cm"),
                                                                   dbItem.get("width_cm"),
                                                                   dbItem.get("length_cm"),
                                                                   dbItem.get("weight_kg"),
                                                                   dbItem.get("quantity"))));
        return items;
    }
}
