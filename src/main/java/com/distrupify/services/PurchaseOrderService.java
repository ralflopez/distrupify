package com.distrupify.services;

import com.distrupify.entities.PurchaseOrderEntity;
import com.distrupify.entities.PurchaseOrderEntity$;
import com.distrupify.requests.PurchaseOrderCreateRequest;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.Date;

import static com.speedment.jpastreamer.streamconfiguration.StreamConfiguration.of;

@ApplicationScoped
public class PurchaseOrderService {

    @Inject
    JPAStreamer jpaStreamer;

    @Inject
    EntityManager em;

    @Transactional
    public void createPurchaseOrder(Long organizationId, PurchaseOrderCreateRequest request) {
        final var purchaseOrder = new PurchaseOrderEntity(organizationId);
        request.items.forEach(i -> purchaseOrder.addLog(i.productId, i.quantity, i.unitPrice));
        purchaseOrder.persist();
    }

    @Transactional
    public void receive(Long organizationId, Long id) {
        final var transaction = jpaStreamer.stream(of(PurchaseOrderEntity.class)
                        .joining(PurchaseOrderEntity$.inventoryTransaction))
                .filter(p -> p.getOrganizationId().equals(organizationId))
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (transaction.isEmpty()) {
            throw new BadRequestException("Purchase order not found");
        }

        transaction.get().setReceivedAt(new Date());
        transaction.get().getInventoryTransaction().setPending(false);
        em.merge(transaction.get());
    }
}
