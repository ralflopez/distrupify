package com.distrupify.repository;

import com.distrupify.entities.InventoryAdjustmentEntity;
import com.distrupify.entities.InventoryAdjustmentEntity$;
import com.distrupify.entities.InventoryLogEntity.InventoryLogType;
import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.exceptions.WebException;
import com.distrupify.resources.requests.InventoryAdjustmentCreateRequest;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.distrupify.entities.InventoryLogEntity.InventoryLogType.INCOMING;

@ApplicationScoped
public class InventoryAdjustmentRepository {

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    ProductRepository productRepository;

    @Transactional
    public long getCount(@Nonnull Long organizationId) {
        return getInventoryAdjustmentStream(organizationId).count();
    }

    @Transactional
    public void create(@Nonnull Long organizationId, @Nonnull InventoryAdjustmentCreateRequest request) {
        if (request.items.isEmpty()) {
            throw new WebException.BadRequest("There should be at least 1 item");
        }

        final var productsMap = productRepository.findAll(organizationId,
                request.items
                        .stream()
                        .map(i -> i.productId)
                        .toList())
                .stream()
                .filter(product -> product.id().isPresent())
                .collect(Collectors.toMap(e -> e.id().get(), e -> e));

        final var productIdQuantityMap = productsMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().quantity()));

        final var invalidProductLog = request.items
                .stream()
                .filter(l -> !productIdQuantityMap.containsKey(l.productId))
                .findAny();

        if (invalidProductLog.isPresent()) {
            throw new WebException.BadRequest("Invalid product id: " + invalidProductLog.get().productId);
        }

        final var invalidProductQuantityOutgoingLog = request.items
                .stream()
                .filter(l -> l.inventoryLogType.equals(InventoryLogType.OUTGOING))
                .filter(l -> productIdQuantityMap.get(l.productId) < l.quantity)
                .findAny();

        if (invalidProductQuantityOutgoingLog.isPresent()) {
            throw new WebException.BadRequest("Insufficient product quantity: " +
                    invalidProductQuantityOutgoingLog.get().quantity);
        }

        final var inventoryAdjustment = new InventoryAdjustmentEntity(organizationId);
        request.items.forEach(i -> {
            final var price = productsMap.get(i.productId).unitPrice();
            final var priceWithSign = i.inventoryLogType.equals(INCOMING) ? price : -price;
            inventoryAdjustment.addLog(i.inventoryLogType, i.productId, i.quantity, priceWithSign);
        });
        inventoryAdjustment.persist();
    }

    /**
     * Note
     * .getInventoryLogs() throws a LazyInitializationException outside this transaction
     * <a href="https://stackoverflow.com/questions/26507446/how-to-resolve-lazyinitializationexception-in-spring-data-jpa#:~:text=if%20you%20try%20to%20get%20the%20lazy%20collection%2C%20you%20will%20get%20that%20exception">Solution</a>
     */
    @Transactional
    public List<InventoryAdjustmentEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        var stream = getInventoryAdjustmentStream(organizationId)
                .peek(a -> {
                    Hibernate.initialize(a.getInventoryTransaction());
                    Hibernate.initialize(a.getInventoryTransaction().getInventoryLogs());
                })
                .sorted(InventoryAdjustmentEntity$.createdAt.reversed());

        if (!pageable.isAll()) {
            stream = stream.skip(pageable.offset())
                    .limit(pageable.limit());
        }

        return stream.toList();

    }

    private Stream<InventoryAdjustmentEntity> getInventoryAdjustmentStream(Long organizationId) {
        return jpaStreamer.stream(InventoryAdjustmentEntity.class)
                .filter(InventoryAdjustmentEntity$.organizationId.equal(organizationId))
                .filter(adj -> !adj.getInventoryTransaction()
                        .getStatus()
                        .equals(InventoryTransactionEntity.InventoryTransactionStatus.DELETED));
    }
}
