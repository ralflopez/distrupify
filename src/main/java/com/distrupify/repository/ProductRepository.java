package com.distrupify.repository;

import com.distrupify.entities.InventoryLogEntity;
import com.distrupify.entities.InventoryLogEntity$;
import com.distrupify.entities.ProductEntity;
import com.distrupify.entities.ProductEntity$;
import com.distrupify.models.ProductModel;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.distrupify.entities.InventoryLogEntity.Type.INCOMING;
import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;
import static jakarta.persistence.criteria.JoinType.LEFT;
import static jakarta.persistence.criteria.JoinType.RIGHT;

@ApplicationScoped
public class ProductRepository {

    @Inject
    JPAStreamer jpaStreamer;

    public List<ProductModel> findAll(Long organizationId) {
        final var products = jpaStreamer.stream(ProductEntity.class)
                .filter(ProductEntity$.organizationId.equal(organizationId))
                .sorted(ProductEntity$.displayName)
                .toList();

        final var nonZeroProductIdQuantityMap = jpaStreamer.stream(of(InventoryLogEntity.class)
                        .joining(InventoryLogEntity$.product, RIGHT)
                        .joining(InventoryLogEntity$.inventoryTransaction, LEFT))
                .filter(Objects::nonNull)
                .filter(i -> i.getOrganizationId().equals(organizationId))
                .collect(Collectors.groupingBy(InventoryLogEntity$.productId.asLong()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(this::getQuantityFromLog)))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().reduce(0, Integer::sum)));

        return products.stream().map(p -> {
            final var qty = nonZeroProductIdQuantityMap.get(p.getId());
            return new ProductModel(Optional.of(p.getId()),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getUnitPrice().doubleValue(),
                    qty == null ? 0 : qty);
        }).toList();
    }

    private int getQuantityFromLog(InventoryLogEntity log) {
        if (log.getInventoryTransaction().isPending()) {
            return 0;
        }

        if (log.getInventoryLogType().equals(INCOMING)) {
            return log.getQuantity();
        }

        return -log.getQuantity();
    }

}
