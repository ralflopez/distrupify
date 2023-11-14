package com.distrupify.services;

import com.distrupify.entities.InventoryDeposit;
import com.distrupify.entities.InventoryLog;
import com.distrupify.requests.InventoryDepositCreateRequest;
import com.distrupify.requests.InventoryDepositSearchRequest;
import com.distrupify.utils.Pageable;
import com.speedment.jpastreamer.application.JPAStreamer;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.distrupify.entities.InventoryDeposit.INVENTORY_TRANSACTION;
import static com.distrupify.entities.InventoryDeposit.ORGANIZATION_ID;
import static com.distrupify.entities.InventoryLog.Type.INCOMING;
import static com.distrupify.entities.InventoryLog.Type.OUTGOING;
import static com.distrupify.entities.InventoryTransaction.TIMESTAMP;
import static com.distrupify.entities.InventoryTransaction.Type.DEPOSIT;
import static com.distrupify.entities.InventoryTransaction.Type.DEPOSIT_ROLLBACK;

@ApplicationScoped
public class InventoryDepositService {
    private static final Logger LOGGER = Logger.getLogger(InventoryDepositService.class);

    @Inject
    InventoryTransactionService inventoryTransactionService;

    @Inject
    InventoryLogService inventoryLogService;

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    EntityManager em;

    public List<InventoryDeposit> findAll(Long organizationId, Page page) {
        final var pageable = Pageable.of(page);
        return jpaStreamer.stream(InventoryDeposit.class)
                .filter(d -> d.getOrganizationId().equals(organizationId))
                .filter(d -> d.getInventoryTransaction() != null)
                .filter(d -> d.getInventoryTransaction().getInventoryLogs() != null)
                .sorted(Comparator.comparing(d -> d.getInventoryTransaction().getTimestamp()))
                .skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }

    public List<InventoryDeposit> search(Long organizationId, InventoryDepositSearchRequest searchRequest, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<InventoryDeposit> criteriaQuery = criteriaBuilder.createQuery(InventoryDeposit.class);

        Root<InventoryDeposit> root = criteriaQuery.from(InventoryDeposit.class);
        root.fetch(INVENTORY_TRANSACTION, JoinType.LEFT);

        final var predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get(ORGANIZATION_ID), organizationId));

        if (searchRequest.hasStartAndEndDate()) {
            Predicate timestampRangePredicate = criteriaBuilder.between(
                    root.get(INVENTORY_TRANSACTION).get(TIMESTAMP),
                    searchRequest.startDate,
                    searchRequest.endDate);
            predicates.add(timestampRangePredicate);
        } else if (searchRequest.hasStartDateOnly()) {
            Predicate timestampFromPredicate = criteriaBuilder.greaterThanOrEqualTo(
                    root.get(INVENTORY_TRANSACTION).get(TIMESTAMP),
                    searchRequest.startDate);
            predicates.add(timestampFromPredicate);
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return em.createQuery(criteriaQuery)
                .setFirstResult(pageable.offset())
                .setMaxResults(pageable.limit())
                .getResultList();
    }

    // TODO: investigate if create request can be validated without using the token
    @Transactional
    public InventoryDeposit deposit(Long organizationId, InventoryDepositCreateRequest inventoryDepositDTO) {
        final var inventoryTransaction = inventoryTransactionService.createTransaction(organizationId, DEPOSIT);

        inventoryDepositDTO.items
                .forEach(i -> inventoryLogService.createInventoryLog(organizationId,
                        INCOMING,
                        inventoryTransaction,
                        i));

        final var inventoryDeposit = InventoryDeposit.builder()
                .organizationId(organizationId)
                .inventoryTransactionId(inventoryTransaction.getId())
                .build();
        inventoryDeposit.persist();

        return inventoryDeposit;
    }

    @Transactional
    public Optional<InventoryDeposit> rollbackDeposit(Long organizationId, Long depositId) {
        return jpaStreamer.stream(InventoryDeposit.class)
                .filter(i -> i.getOrganizationId().equals(organizationId))
                .filter(i -> i.getId().equals(depositId))
                .filter(i -> i.getInventoryTransaction() != null)
                .filter(i -> i.getInventoryTransaction().getInventoryLogs() != null)
                .peek(i -> {
                    final var inverseLogs = i.getInventoryTransaction().getInventoryLogs().stream().map(l ->
                                    InventoryLog.builder()
                                            .organizationId(organizationId)
                                            .inventoryLogType(OUTGOING)
                                            .productId(l.getProductId())
                                            .price(l.getPrice())
                                            .quantity(l.getQuantity())
                                            .inventoryTransactionId(l.getInventoryTransactionId())
                                            .build())
                            .peek(l -> l.persist())
                            .toList();

                    final var inverseTransaction = inventoryTransactionService.createTransaction(organizationId, DEPOSIT_ROLLBACK);
                    inverseTransaction.setInventoryLogs(inverseLogs);

                    final var inverseDeposit = InventoryDeposit.builder()
                            .organizationId(organizationId)
                            .inventoryTransactionId(inverseTransaction.getId())
                            .build();
                    inverseDeposit.persist();
                })
                .findAny();
    }

}
