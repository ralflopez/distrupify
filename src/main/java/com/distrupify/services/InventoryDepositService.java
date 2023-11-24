package com.distrupify.services;

import com.distrupify.entities.InventoryDepositEntity;
import com.distrupify.repository.InventoryTransactionRepository;
import com.distrupify.requests.InventoryDepositCreateRequest;
import com.distrupify.requests.InventoryDepositSearchRequest;
import com.distrupify.models.InventoryLogModel;
import com.distrupify.models.InventoryTransactionModel;
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

import static com.distrupify.entities.InventoryDepositEntity.INVENTORY_TRANSACTION;
import static com.distrupify.entities.InventoryDepositEntity.ORGANIZATION_ID;
import static com.distrupify.entities.InventoryTransactionEntity.TIMESTAMP;

@ApplicationScoped
public class InventoryDepositService {
    private static final Logger LOGGER = Logger.getLogger(InventoryDepositService.class);

    @Inject
    InventoryTransactionRepository inventoryTransactionRepository;

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    EntityManager em;

    public List<InventoryDepositEntity> findAll(Long organizationId, Page page) {
        final var pageable = Pageable.of(page);
        return jpaStreamer.stream(InventoryDepositEntity.class)
                .filter(d -> d.getOrganizationId().equals(organizationId))
                .filter(d -> d.getInventoryTransaction() != null)
                .filter(d -> d.getInventoryTransaction().getInventoryLogs() != null)
                .sorted(Comparator.comparing(d -> d.getInventoryTransaction().getTimestamp()))
                .skip(pageable.offset())
                .limit(pageable.limit())
                .toList();
    }

    public List<InventoryDepositEntity> search(Long organizationId, InventoryDepositSearchRequest searchRequest, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<InventoryDepositEntity> criteriaQuery = criteriaBuilder.createQuery(InventoryDepositEntity.class);

        Root<InventoryDepositEntity> root = criteriaQuery.from(InventoryDepositEntity.class);
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

    @Transactional
    public void deposit(InventoryTransactionModel<InventoryTransactionModel.Type.InventoryDeposit> model) {
        inventoryTransactionRepository.persist(model);
    }

//    @Transactional
//    public Optional<InventoryDeposit> rollbackDeposit(Long organizationId, Long depositId) {
//        return jpaStreamer.stream(InventoryDeposit.class)
//                .filter(i -> i.getOrganizationId().equals(organizationId))
//                .filter(i -> i.getId().equals(depositId))
//                .filter(i -> i.getInventoryTransaction() != null)
//                .filter(i -> i.getInventoryTransaction().getInventoryLogs() != null)
//                .peek(i -> {
//                    final var inverseLogs = i.getInventoryTransaction().getInventoryLogs().stream().map(l ->
//                                    InventoryLog.builder()
//                                            .organizationId(organizationId)
//                                            .inventoryLogType(OUTGOING)
//                                            .productId(l.getProductId())
//                                            .unitPrice(l.getPrice())
//                                            .quantity(l.getQuantity())
//                                            .inventoryTransactionId(l.getInventoryTransactionId())
//                                            .build())
//                            .peek(l -> l.persist())
//                            .toList();
//
//                    final var inverseTransaction = inventoryTransactionService.createTransaction(organizationId, DEPOSIT_ROLLBACK);
//                    inverseTransaction.setInventoryLogs(inverseLogs);
//
//                    final var inverseDeposit = InventoryDeposit.builder()
//                            .organizationId(organizationId)
//                            .inventoryTransactionId(inverseTransaction.getId())
//                            .build();
//                    inverseDeposit.persist();
//                })
//                .findAny();
//    }

}
