package br.com.luizfp.cursobranas.infra.dao;

import br.com.luizfp.cursobranas.application.query.GetOrderItemOutput;
import br.com.luizfp.cursobranas.application.query.GetOrderOutput;
import br.com.luizfp.cursobranas.application.query.OrderDao;
import br.com.luizfp.cursobranas.infra.database.DatabaseConnection;
import br.com.luizfp.cursobranas.infra.database.DatabaseResultRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class OrderDaoDatabase implements OrderDao {
    @NotNull
    private final DatabaseConnection databaseConnection;

    public OrderDaoDatabase(@NotNull final DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @NotNull
    @Override
    public GetOrderOutput getOrder(@NotNull final Long orderId) {
        return databaseConnection
                .runInTransaction(tx -> {
                    final DatabaseResultRow dbOrder = tx.query("select * from orders o where o.id = ?", orderId);
                    final var items = createOrderItems(tx, orderId);
                    return new GetOrderOutput(orderId,
                                              dbOrder.get("code"),
                                              items);
                });
    }

    @NotNull
    @Override
    public List<GetOrderOutput> getOrders() {
        return databaseConnection
                .runInTransaction(tx -> {
                    final List<DatabaseResultRow> dbOrders = tx.query("select * from orders");
                    final List<GetOrderOutput> orders = new ArrayList<>();
                    for (final DatabaseResultRow dbOrder : dbOrders) {
                        final Long orderId = dbOrder.get("id");
                        final var items = createOrderItems(tx, orderId);
                        final GetOrderOutput order = new GetOrderOutput(orderId,
                                                                        dbOrder.get("code"),
                                                                        items);
                        orders.add(order);
                    }
                    return orders;
                });
    }

    @NotNull
    private List<GetOrderItemOutput> createOrderItems(@NotNull final DatabaseConnection tx,
                                                      @NotNull final Long orderId) {
        final List<DatabaseResultRow> dbItems = tx.query("""
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
