package com.distrupify.repository;

import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.entities.SalesEntity;
import com.distrupify.entities.SalesEntity$;
import com.distrupify.exceptions.WebException;
import com.distrupify.models.ProductModel;
import com.distrupify.resources.requests.SalesCreateRequest;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@ApplicationScoped
public class SalesRepository {

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    ProductRepository productRepository;

    @Transactional
    public long getCount(@Nonnull Long organizationId) {
        return getSalesStream(organizationId).count();
    }

    @Transactional
    public List<SalesEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        if (pageable.isAll()) {
            return getSalesStream(organizationId)
                    .toList();
        }

        final var stream = getSalesStream(organizationId)
                .peek(s -> {
                    Hibernate.initialize(s.getInventoryTransaction().getInventoryLogs());
                });

        return stream
                .skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }

    @Transactional
    public void softDelete(@Nonnull Long organizationId, @Nonnull Long salesId) {
        final var product = getSalesStream(organizationId)
                .filter(SalesEntity$.id.equal(salesId))
                .peek(p -> {
                    p.getInventoryTransaction().setStatus(InventoryTransactionEntity.InventoryTransactionStatus.DELETED);
                })
                .findAny();

        if (product.isEmpty()) {
            throw new WebException.BadRequest("Sales not found");
        }
    }

    @Transactional
    public void create(@Nonnull Long organizationId, @Nonnull SalesCreateRequest request) {
        if (request.items.isEmpty()) {
            throw new WebException.BadRequest("There should be at least 1 item");
        }

        final var productIdQuantityMap = productRepository.findAll(organizationId)
                .stream()
                .filter(p -> p.id().isPresent())
                .collect(Collectors.toMap(p -> p.id().get(), ProductModel::quantity));

        final var invalidProductIdItem = request.items
                .stream()
                .filter(l -> !productIdQuantityMap.containsKey(l.productId))
                .findAny();

        if (invalidProductIdItem.isPresent()) {
            throw new WebException.BadRequest("Invalid product id: " + invalidProductIdItem.get().productId);
        }

        final var insufficientQuantityProducts = request.items
                .stream()
                .filter(l -> productIdQuantityMap.get(l.productId) < l.quantity)
                .findAny();

        if (insufficientQuantityProducts.isPresent()) {
            throw new WebException.BadRequest("Insufficient product quantity: " +
                    insufficientQuantityProducts.get().quantity +
                    " for product id: " +
                    insufficientQuantityProducts.get().productId);
        }

        final var sales = new SalesEntity(organizationId, request.customerId);
        request.items.forEach(i -> sales.addLog(i.productId, i.quantity, i.unitPrice));
        sales.persist();
    }

    private Stream<SalesEntity> getSalesStream(@Nonnull Long organizationId) {
        return jpaStreamer.stream(of(SalesEntity.class).joining(SalesEntity$.inventoryTransaction))
                .filter(SalesEntity$.organizationId.equal(organizationId))
                .filter(s -> !s.getInventoryTransaction()
                        .getStatus()
                        .equals(InventoryTransactionEntity.InventoryTransactionStatus.DELETED));
    }
}
