package com.distrupify.services;

import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.entities.InventoryTransactionEntity$;
import com.distrupify.requests.InventoryTransactionSearchRequest;
import com.distrupify.utils.DateUtil;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@ApplicationScoped
public class InventoryTransactionService {

    @Inject
    JPAStreamer jpaStreamer;

    public List<InventoryTransactionEntity> findAll(Long organizationId, Pageable pageable) {
        return jpaStreamer.stream(of(InventoryTransactionEntity.class)
                        .joining(InventoryTransactionEntity$.inventoryLogs))
                .filter(t -> t.getOrganizationId().equals(organizationId))
                .sorted(Comparator.comparing(InventoryTransactionEntity::getTimestamp).reversed())
                .skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }

    public List<InventoryTransactionEntity> searchAll(Long organizationId,
                                                      InventoryTransactionSearchRequest request,
                                                      Pageable pageable,
                                                      boolean asc) throws ParseException {
        var stream = jpaStreamer.stream(of(InventoryTransactionEntity.class)
                        .joining(InventoryTransactionEntity$.inventoryLogs))
                .filter(t -> t.getOrganizationId().equals(organizationId));

        // Date
        if (request.hasStartAndEndDate()) {
            final var startDate = DateUtil.from(request.startDate);
            final var endDate = DateUtil.from(request.endDate);
            stream = stream.filter(InventoryTransactionEntity$.timestamp.greaterOrEqual(startDate))
                    .filter(InventoryTransactionEntity$.timestamp.lessOrEqual(endDate));
        } else if (request.hasStartDateOnly()) {
            final var startDate = DateUtil.from(request.startDate);
            stream = stream.filter(InventoryTransactionEntity$.timestamp.greaterOrEqual(startDate));
        }

        // Sort
        if (asc) {
            stream = stream.sorted(Comparator.comparing(InventoryTransactionEntity::getTimestamp));
        } else {
            stream = stream.sorted(Comparator.comparing(InventoryTransactionEntity::getTimestamp).reversed());
        }

        return stream.skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }
}
