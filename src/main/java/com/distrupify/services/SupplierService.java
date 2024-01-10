package com.distrupify.services;

import com.distrupify.entities.SupplierEntity;
import com.distrupify.repository.SupplierRepository;
import com.distrupify.resources.requests.SupplierCreateRequest;
import com.distrupify.resources.requests.SupplierEditRequest;
import com.distrupify.utils.Pageable;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SupplierService {
    @Inject
    SupplierRepository supplierRepository;

    public List<SupplierEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return supplierRepository.findAll(organizationId, pageable);
    }

    public int getPageCount(@Nonnull Long organizationId, Pageable pageable) {
        if (pageable.isAll()) {
            return 1;
        }

        final var productCount = supplierRepository.getCount(organizationId);
        return (int) Math.ceilDiv(productCount, pageable.limit());
    }

    public void create(@Nonnull Long organizationId, @Nonnull SupplierCreateRequest request) {
        supplierRepository.create(organizationId, request);
    }

    public void softDelete(@Nonnull Long organizationId, @Nonnull Long productId) {
        supplierRepository.softDelete(organizationId, productId);
    }

    public void edit(@Nonnull Long organizationId, @Nonnull Long productId, @Nonnull SupplierEditRequest request) {
        supplierRepository.edit(organizationId, productId, request);
    }

}
