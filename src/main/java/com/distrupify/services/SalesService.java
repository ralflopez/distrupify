package com.distrupify.services;

import com.distrupify.entities.SalesEntity;
import com.distrupify.repository.SalesRepository;
import com.distrupify.requests.SalesCreateRequest;
import com.distrupify.utils.Pageable;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class SalesService {

    @Inject
    SalesRepository salesRepository;

    public int getPageCount(@Nonnull Long organizationId, int pageSize) {
        final var salesCount = salesRepository.getCount(organizationId);
        return (int) Math.ceilDiv(salesCount, pageSize);
    }

    public List<SalesEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return salesRepository.findAll(organizationId, pageable);
    }

    @Transactional
    public void softDelete(@Nonnull Long organizationId, @Nonnull Long salesId) {
        salesRepository.softDelete(organizationId, salesId);
    }

    public void create(@Nonnull Long organizationId, @Nonnull SalesCreateRequest request) {
        salesRepository.create(organizationId, request);
    }

    public static BigDecimal computeTotalPrice(@Nonnull SalesEntity sales) {
        final var logs = sales.getInventoryTransaction().getInventoryLogs();
        return logs.stream().map(log -> {
            final var price = log.getUnitPrice();
            final var qty = BigDecimal.valueOf(log.getQuantity());
            return price.multiply(qty);
        }).reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }
}
