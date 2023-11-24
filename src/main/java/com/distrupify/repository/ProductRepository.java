package com.distrupify.repository;

import com.distrupify.entities.InventoryLogEntity;
import com.distrupify.entities.InventoryLogEntity$;
import com.distrupify.entities.ProductEntity;
import com.distrupify.entities.ProductEntity$;
import com.distrupify.models.ProductModel;
import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static jakarta.persistence.criteria.JoinType.LEFT;

@ApplicationScoped
public class ProductRepository {

    @Inject
    JPAStreamer jpaStreamer;

    public List<ProductModel> findAll(Long organizationId) {
        final var products = jpaStreamer.stream(ProductEntity.class)
                .filter(ProductEntity$.organizationId.equal(organizationId))
                .sorted(ProductEntity$.displayName)
                .toList();

        final var productIdQuantityMap = jpaStreamer.stream(StreamConfiguration.of(InventoryLogEntity.class)
                        .joining(InventoryLogEntity$.product, LEFT)
                        .joining(InventoryLogEntity$.inventoryTransaction, LEFT))
                .filter(InventoryLogEntity$.organizationId.equal(organizationId))
                .collect(Collectors.groupingBy(InventoryLogEntity$.productId.asLong()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().stream().map(this::getQuantityFromLog)))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().reduce(0, Integer::sum)));

        return products.stream().map(p -> {
            final var qty = productIdQuantityMap.get(p.getId());
            return new ProductModel(Optional.of(p.getId()),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getUnitPrice().doubleValue(),
                    qty);
        }).toList();
    }

    private int getQuantityFromLog(InventoryLogEntity log) {
        if (log.getInventoryTransaction().isPending()) {
            return 0;
        }

        if (log.getInventoryTransaction().isIncoming()) {
            return log.getQuantity();
        }

        return -log.getQuantity();
    }

}
