package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.domain.entity.*;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockEntryRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.Collection;

import static br.com.luizfp.cursobranas.domain.entity.StockEntryOperation.IN;

public final class PlaceOrder {
    @NotNull
    private final CouponRepository couponRepository;
    @NotNull
    private final OrderRepository orderRepository;
    @NotNull
    private final StockItemRepository stockItemRepository;
    @NotNull
    private final StockEntryRepository stockEntryRepository;

    public PlaceOrder(@NotNull final AbstractRepositoryFactory factory) {
        this.couponRepository = factory.createCouponRepository();
        this.orderRepository = factory.createOrderRepository();
        this.stockItemRepository = factory.createStockItemRepository();
        this.stockEntryRepository = factory.createStockEntryRepository();
    }

    @NotNull
    public PlaceOrderOutput execute(@NotNull final PlaceOrderInput input,
                                    @NotNull final OffsetDateTime orderCreatedAt) {
        final long sequence = orderRepository.nextSequence();
        final Order order = new Order(input.cpf(), orderCreatedAt, sequence);
        input.orderItemInput().forEach(inputItem -> {
            final StockItem stockItem = stockItemRepository.getById(inputItem.itemId());
            final Collection<StockEntry> entries = stockEntryRepository.getByItemId(inputItem.itemId());
            final int quantityAvailable = countAvailableStockQuantity(entries);
            if (inputItem.quantity() > quantityAvailable) {
                throw new OutOfStockException(inputItem.itemId(),
                                              quantityAvailable,
                                              inputItem.quantity());
            }
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

    private int countAvailableStockQuantity(@NotNull final Collection<StockEntry> entries) {
        return entries
                .stream()
                .mapToInt(entry -> entry.operation() == IN ? entry.quantity() : -entry.quantity())
                .sum();
    }
}
