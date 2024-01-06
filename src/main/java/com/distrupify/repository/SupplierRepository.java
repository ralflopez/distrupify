package com.distrupify.repository;

import com.distrupify.entities.SupplierEntity;
import com.distrupify.entities.SupplierEntity$;
import com.distrupify.exceptions.WebException;
import com.distrupify.resources.requests.SupplierCreateRequest;
import com.distrupify.resources.requests.SupplierEditRequest;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class SupplierRepository {

    @Inject
    JPAStreamer jpaStreamer;

    public long getCount(@Nonnull Long organizationId) {
        return getSupplierStream(organizationId).count();
    }

    @Transactional
    public void create(@Nonnull Long organizationId, @Nonnull SupplierCreateRequest request) {
        if (getSupplierStream(organizationId).anyMatch(SupplierEntity$.name.equal(request.name.trim()))) {
            throw new WebException.BadRequest(String.format("Supplier with name: %s already exists", request.name));
        }

        final var newSupplier = new SupplierEntity(null, request.name.trim(),
                request.address.trim(), request.contactNumber.trim(),
                organizationId, null, false);

        newSupplier.persist();
    }

    @Transactional
    public void softDelete(@Nonnull Long organizationId, @Nonnull Long supplierId) {
        final var supplier = getSupplierStream(organizationId)
                .filter(SupplierEntity$.id.equal(supplierId))
                .peek(sup -> {
                    sup.setDeleted(true);
                })
                .findAny();

        if (supplier.isEmpty()) {
            throw new WebException.BadRequest("Supplier not found");
        }
    }

    @Transactional
    public void edit(@Nonnull Long organizationId, @Nonnull Long productId, @Nonnull SupplierEditRequest request) {
        final var product = getSupplierStream(organizationId)
                .filter(SupplierEntity$.id.equal(productId))
                .peek(sup -> {
                    sup.setAddress(request.address.trim());
                    sup.setName(request.address.trim());
                    sup.setContactNumber(request.contactNumber.trim());
                })
                .findAny();

        if (product.isEmpty()) {
            throw new WebException.BadRequest("Product not found");
        }
    }

    @Transactional
    public List<SupplierEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        if (pageable.isAll()) {
            return getSupplierStream(organizationId)
                    .toList();
        }

        return getSupplierStream(organizationId)
                .skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }

    private Stream<SupplierEntity> getSupplierStream(@Nonnull Long organizationId) {
        return jpaStreamer.stream(SupplierEntity.class)
                .filter(SupplierEntity$.organizationId.equal(organizationId))
                .filter(SupplierEntity$.deleted.notEqual(true))
                .sorted(SupplierEntity$.name);
    }

}
