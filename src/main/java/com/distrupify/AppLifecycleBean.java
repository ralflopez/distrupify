package com.distrupify;

import com.distrupify.entities.Organization;
import com.distrupify.entities.Product;
import com.distrupify.inventory.transaction.deposit.InventoryDepositService;
import com.distrupify.inventory.transaction.deposit.request.CreateInventoryDepositRequest;
import com.distrupify.inventory.transaction.deposit.request.InventoryDepositSearchRequest;
import com.distrupify.util.Pageable;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Inject
    InventoryDepositService inventoryDepositService;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        final var testOrganization = Organization.builder()
                .name("test-organization")
                .displayName("Test Organization")
                .build();
        testOrganization.persist();

        final var galaxyBuds2 = Product.builder()
                .organizationId(testOrganization.getId())
                .sku("123456789")
                .brand("Samsung")
                .name("Galaxy Buds 2")
                .description("")
                .price(BigDecimal.valueOf(4000))
                .build();
        galaxyBuds2.persist();

        final var deposit1 = CreateInventoryDepositRequest.builder()
                .items(List.of(
                        CreateInventoryDepositRequest.Item.builder()
                                .price(3000.0)
                                .productId(galaxyBuds2.getId())
                                .quantity(100)
                                .build()
                ))
                .build();

        final var d = inventoryDepositService.deposit(testOrganization.getId(), deposit1);

        inventoryDepositService.rollbackDeposit(testOrganization.getId(), d.getId());

        final var searchRequest = InventoryDepositSearchRequest.builder()
                .startDate(null)
                .build();

        System.out.println(inventoryDepositService.search(testOrganization.getId(), searchRequest, Pageable.of(1, 10)));
    }

}