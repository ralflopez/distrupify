package com.distrupify.repository;

import com.distrupify.resources.dto.InventoryTransactionDTO;
import com.distrupify.entities.InventoryTransactionEntity;
import com.distrupify.entities.InventoryTransactionEntity$;
import com.distrupify.exceptions.WebException;
import com.distrupify.resources.requests.InventoryTransactionSearchRequest;
import com.distrupify.utils.DateUtil;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@ApplicationScoped
public class InventoryTransactionRepository {

    @Inject
    JPAStreamer jpaStreamer;

    @Transactional
    public long getCount(@Nonnull Long organizationId) {
        return getInventoryTransactionStream(organizationId).count();
    }

    @Transactional
    public List<InventoryTransactionEntity> findAll(@Nonnull Long organizationId, @Nonnull Pageable pageable) {
        return jpaStreamer.stream(of(InventoryTransactionEntity.class)
                        .joining(InventoryTransactionEntity$.inventoryLogs))
                .filter(t -> t.getOrganizationId().equals(organizationId))
                .sorted(Comparator.comparing(InventoryTransactionEntity::getTimestamp).reversed())
                .skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }

    @Transactional
    public List<InventoryTransactionDTO> find(Long organizationId,
                                              InventoryTransactionSearchRequest request,
                                              Pageable pageable,
                                              boolean asc) throws ParseException {
        var stream = getInventoryTransactionStream(organizationId);

        // Type
        if (!request.type.isEmpty()) {
            stream = stream.filter(InventoryTransactionEntity$.inventoryTransactionType.in(request.type));
        }

        // Status
        if (!request.status.isEmpty()) {
            stream = stream.filter(InventoryTransactionEntity$.status.in(request.status));
        }

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

        // Pagination
        stream = stream.skip(pageable.offset())
                .limit(pageable.limit());

        // Value
        var dtoStream = stream.map(transaction -> {
            final var value = transaction.getInventoryLogs()
                    .stream()
                    .map(t -> t.getUnitPrice().multiply(BigDecimal.valueOf(t.getQuantity())))
                    .reduce(BigDecimal.ZERO, (BigDecimal::add))
                    .doubleValue();

            return InventoryTransactionDTO.fromEntity(transaction, value);
        });

        return dtoStream.toList();
    }

//    public double getTransactionValues(@Nonnull Long organizationId, @Nonnull List<Long> transactionId) {
//        return getInventoryTransactionStream(organizationId)
//                .filter(InventoryTransactionEntity$.id.in(transactionId))
//                .collect(Collectors.groupingBy(InventoryTransactionEntity::getId))
//                .entrySet()
//                .stream()
//    }

    @Transactional
    public void softDelete(@Nonnull Long organizationId, @Nonnull Long transactionId) {
        final var transaction = getInventoryTransactionStream(organizationId)
                .filter(InventoryTransactionEntity$.id.equal(transactionId))
                .peek(t -> {
                    t.setStatus(InventoryTransactionEntity.InventoryTransactionStatus.DELETED);
                })
                .findAny();

        if (transaction.isEmpty()) {
            throw new WebException.BadRequest("Transaction not found");
        }
    }

    private Stream<InventoryTransactionEntity> getInventoryTransactionStream(@Nonnull Long organizationId) {
        return jpaStreamer.stream(of(InventoryTransactionEntity.class)
                        .joining(InventoryTransactionEntity$.inventoryLogs))
                .filter(InventoryTransactionEntity$.organizationId.equal(organizationId));
    }
}
