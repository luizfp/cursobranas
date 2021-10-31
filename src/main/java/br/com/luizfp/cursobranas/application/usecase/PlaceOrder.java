package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.domain.entity.Coupon;
import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.StockItem;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public final class PlaceOrder {
    @NotNull
    private final CouponRepository couponRepository;
    @NotNull
    private final OrderRepository orderRepository;
    @NotNull
    private final StockItemRepository stockItemRepository;

    public PlaceOrder(@NotNull final AbstractRepositoryFactory factory) {
        this.couponRepository = factory.createCouponRepository();
        this.orderRepository = factory.createOrderRepository();
        this.stockItemRepository = factory.createStockItemRepository();
    }

    @NotNull
    public PlaceOrderOutput execute(@NotNull final PlaceOrderInput input,
                                    @NotNull final OffsetDateTime orderCreatedAt) {
        final long sequence = orderRepository.nextSequence();
        final Order order = new Order(input.cpf(), orderCreatedAt, sequence);
        input.orderItemInput().forEach(inputItem -> {
            final StockItem stockItem = stockItemRepository.getById(inputItem.itemId());
//            if (inputItem.quantity() > stockItem.quantityAvailable()) {
//                throw new InsufficientStockItemsException(inputItem.itemId(),
//                                                          stockItem.quantityAvailable(),
//                                                          inputItem.quantity());
//            }
            order.addItem(stockItem, inputItem.quantity());
        });
        if (input.couponCode() != null) {
            final Coupon coupon = couponRepository.getByCode(input.couponCode());
            order.applyCoupon(coupon);
        }
        final Long orderId = orderRepository.save(order);
        final String orderCode = order.getOrderCode();
        return new PlaceOrderOutput(orderId, orderCode, order.calculateOrderTotal().doubleValue());
    }
}
