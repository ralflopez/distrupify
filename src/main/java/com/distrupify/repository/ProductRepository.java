package com.distrupify.repository;

import com.distrupify.entities.InventoryLogEntity;
import com.distrupify.entities.InventoryLogEntity$;
import com.distrupify.entities.ProductEntity;
import com.distrupify.entities.ProductEntity$;
import com.distrupify.exceptions.WebException;
import com.distrupify.models.ProductModel;
import com.distrupify.resources.requests.ProductCreateRequest;
import com.distrupify.resources.requests.ProductEditRequest;
import com.distrupify.resources.requests.ProductSearchFilterBy;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.distrupify.entities.InventoryLogEntity.InventoryLogType.INCOMING;
import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;
import static jakarta.persistence.criteria.JoinType.LEFT;
import static jakarta.persistence.criteria.JoinType.RIGHT;

@ApplicationScoped
public class ProductRepository {

    @Inject
    JPAStreamer jpaStreamer;



    @Transactional
    public void create(@Nonnull Long organizationId, @Nonnull ProductCreateRequest request) {
        final var displayName = request.brand + " " + request.name + " " + request.description;

        if (getProductStream(organizationId).anyMatch(ProductEntity$.sku.equal(request.sku.trim()))) {
            throw new WebException.BadRequest(String.format("Product with SKU: %s already exists", request.sku));
        }

        final var newProduct = new ProductEntity(null, request.sku.trim(), request.brand.trim(),
                request.name.trim(), displayName.trim(), request.description.trim(),
                BigDecimal.valueOf(request.unitPrice), organizationId, null,
                null, false);

        newProduct.persist();
    }

    @Transactional
    public void softDelete(@Nonnull Long organizationId, @Nonnull Long productId) {
        final var product = jpaStreamer.stream(ProductEntity.class)
                .filter(ProductEntity$.organizationId.equal(organizationId))
                .filter(ProductEntity$.id.equal(productId))
                .peek(p -> {
                    p.setDeleted(true);
                    p.setSku(null);
                })
                .findAny();

        if (product.isEmpty()) {
            throw new WebException.BadRequest("Product not found");
        }
    }

    @Transactional
    public void edit(@Nonnull Long organizationId, @Nonnull Long productId, @Nonnull ProductEditRequest request) {
        final var product = getProductStream(organizationId)
                .filter(ProductEntity$.id.equal(productId))
                .peek(p -> {
                    p.setBrand(request.brand.trim());
                    p.setName(request.name.trim());
                    p.setDescription(request.description.trim());
                    p.setSku(request.sku.trim());
                    p.setUnitPrice(BigDecimal.valueOf(request.unitPrice));
                })
                .findAny();

        if (product.isEmpty()) {
            throw new WebException.BadRequest("Product not found");
        }
    }

    public List<ProductModel> findAll(@Nonnull Long organizationId, @Nonnull List<Long> productIds) {
        return buildProductWithQuantity(organizationId,
                getProductStream(organizationId)
                        .filter(ProductEntity$.id.in(productIds)),
                Pageable.all());
    }

    public List<ProductModel> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return buildProductWithQuantity(organizationId, getProductStream(organizationId), pageable);
    }

    public long getCount(@Nonnull Long organizationId) {
        return getProductStream(organizationId).count();
    }

    public List<ProductModel> findAll(@Nonnull Long organizationId) {
        return buildProductWithQuantity(organizationId, getProductStream(organizationId), Pageable.all());
    }

    public List<ProductModel> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable,
                                      @Nonnull String searchString, @Nonnull ProductSearchFilterBy filterBy) {
        final var productStream = switch (filterBy) {
            case SKU -> getProductStream(organizationId)
                    .filter(ProductEntity$.sku.containsIgnoreCase(searchString));
            case NAME -> getProductStream(organizationId)
                    .filter(ProductEntity$.displayName.containsIgnoreCase(searchString));
        };

        return buildProductWithQuantity(organizationId, productStream, pageable);
    }

    public long getCount(@Nonnull Long organizationId, @Nonnull String searchString, ProductSearchFilterBy filterBy) {
        final var productStream = switch (filterBy) {
            case SKU -> getProductStream(organizationId)
                    .filter(ProductEntity$.sku.containsIgnoreCase(searchString));
            case NAME -> getProductStream(organizationId)
                    .filter(ProductEntity$.displayName.containsIgnoreCase(searchString));
        };

        return productStream.count();
    }

    private Stream<ProductEntity> getProductStream(@Nonnull Long organizationId) {
        return jpaStreamer.stream(ProductEntity.class)
                .filter(ProductEntity$.organizationId.equal(organizationId))
                .filter(ProductEntity$.deleted.notEqual(true))
                .sorted(ProductEntity$.displayName);
    }

    private List<ProductModel> buildProductWithQuantity(Long organizationId, Stream<ProductEntity> products,
                                                        Pageable pageable) {
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

        var stream = products.map(p -> {
            final var qty = nonZeroProductIdQuantityMap.get(p.getId());
            return new ProductModel(Optional.of(p.getId()),
                    p.getSku(),
                    p.getBrand(),
                    p.getName(),
                    p.getDescription(),
                    p.getUnitPrice().doubleValue(),
                    qty == null ? 0 : qty);
        });

        if (!pageable.isAll()) {
            stream = stream.skip(pageable.offset())
                    .limit(pageable.limit());
        }

        return stream.toList();
    }

    private int getQuantityFromLog(InventoryLogEntity log) {
        return switch (log.getInventoryTransaction().getStatus()) {
            case PENDING, DELETED -> 0;
            case VALID -> log.getInventoryLogType().equals(INCOMING) ? log.getQuantity() : -log.getQuantity();
        };
    }

}
