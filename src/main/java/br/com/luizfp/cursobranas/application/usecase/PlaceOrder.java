package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.StockItem;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

public final class PlaceOrder {
    @NotNull
    private final ValidateCoupon validateCoupon;
    @NotNull
    private final OrderRepository orderRepository;
    @NotNull
    private final StockItemRepository stockItemRepository;

    public PlaceOrder(@NotNull final ValidateCoupon validateCoupon,
                      @NotNull final OrderRepository orderRepository,
                      @NotNull final StockItemRepository stockItemRepository) {
        this.validateCoupon = validateCoupon;
        this.orderRepository = orderRepository;
        this.stockItemRepository = stockItemRepository;
    }

    @NotNull
    public PlaceOrderOutput execute(@NotNull final PlaceOrderInput input,
                                    @NotNull final OffsetDateTime orderCreatedAt) {
        if (input.couponCode() != null) {
            validateCoupon.execute(input.couponCode(), orderCreatedAt);
        }
        final long sequence = orderRepository.nextSequence();
        final Order order = new Order(input.cpf(), orderCreatedAt, sequence);
        input.orderItemInput().forEach(inputItem -> {
            final StockItem stockItem = stockItemRepository.getById(inputItem.itemId());
            order.addItem(stockItem, inputItem.quantity());
        });
        final Long orderId = orderRepository.save(order);
        final String orderCode = order.getOrderCode();
        return new PlaceOrderOutput(orderId, orderCode, order.calculateOrderTotal().doubleValue());
    }
}
