package br.com.luizfp.cursobranas.application.usecase;

import br.com.luizfp.cursobranas.application.dto.PlaceOrderInput;
import br.com.luizfp.cursobranas.application.dto.PlaceOrderOutput;
import br.com.luizfp.cursobranas.domain.entity.Order;
import br.com.luizfp.cursobranas.domain.entity.OutOfStockException;
import br.com.luizfp.cursobranas.domain.factory.AbstractRepositoryFactory;
import br.com.luizfp.cursobranas.domain.repository.CouponRepository;
import br.com.luizfp.cursobranas.domain.repository.OrderRepository;
import br.com.luizfp.cursobranas.domain.repository.StockEntryRepository;
import br.com.luizfp.cursobranas.domain.repository.StockItemRepository;
import br.com.luizfp.cursobranas.domain.service.StockCalculator;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

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
        final var stockCalculator = new StockCalculator();
        final var sequence = orderRepository.nextSequence();
        final var order = new Order(input.cpf(), orderCreatedAt, sequence);
        input.orderItemInput().forEach(inputItem -> {
            final var stockItem = stockItemRepository.getById(inputItem.itemId());
            final var entries = stockEntryRepository.getByItemId(inputItem.itemId());
            final int quantityAvailable = stockCalculator.countAvailableStockQuantity(entries);
            if (inputItem.quantity() > quantityAvailable) {
                throw new OutOfStockException(inputItem.itemId(),
                                              quantityAvailable,
                                              inputItem.quantity());
            }
            order.addItem(stockItem, inputItem.quantity());
        });
        if (input.couponCode() != null) {
            final var coupon = couponRepository.getByCode(input.couponCode());
            order.applyCoupon(coupon);
        }
        final var orderId = orderRepository.save(order);
        final var orderCode = order.getOrderCode();
        return new PlaceOrderOutput(orderId, orderCode, order.calculateOrderTotal().doubleValue());
    }
}
