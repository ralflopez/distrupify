package com.distrupify;

import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.OrganizationEntity;
import com.distrupify.entities.ProductEntity;
import com.distrupify.services.InventoryDepositService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Inject
    InventoryDepositService inventoryDepositService;

    @Inject
    AuthService authService;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        final var testOrganization = OrganizationEntity.builder()
                .name("test-organization")
                .displayName("Test Organization")
                .build();
        testOrganization.persist();

        final var galaxyBuds2 = ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("123456789")
                .brand("Samsung")
                .name("Galaxy Buds 2")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build();
        galaxyBuds2.persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("123422378")
                .brand("Samsung")
                .name("Galaxy Buds 3")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("523349087")
                .brand("Samsung")
                .name("Galaxy Buds 4")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("768334123")
                .brand("Samsung")
                .name("Galaxy Buds 5")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("244312345")
                .brand("Samsung")
                .name("Galaxy Buds 6")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("234223342")
                .brand("Samsung")
                .name("Galaxy Buds 7")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("334231221")
                .brand("Samsung")
                .name("Galaxy Buds 8")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("234458777")
                .brand("Samsung")
                .name("Galaxy Buds 9")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("998678777")
                .brand("Samsung")
                .name("Galaxy Buds 10")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("123452134")
                .brand("Samsung")
                .name("Galaxy Buds 11")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("553234567")
                .brand("Samsung")
                .name("Galaxy Buds 12")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("653455896")
                .brand("Samsung")
                .name("Galaxy Buds 13")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("544673444")
                .brand("Samsung")
                .name("Galaxy Buds 14")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        ProductEntity.builder()
                .organizationId(testOrganization.getId())
                .sku("332344566")
                .brand("Samsung")
                .name("Galaxy Buds 15")
                .description("")
                .unitPrice(BigDecimal.valueOf(4000))
                .build()
                .persist();

        final var testUser1 = authService.signup(SignupRequest.builder()
                .email("test-user@email.com")
                .password("password")
                .name("Test User 1")
                .organizationId(testOrganization.getId())
                .build());
        LOGGER.info("Token: " + testUser1.token);

//        final var deposit1 = CreateInventoryDepositRequest.builder()
//                .items(List.of(
//                        CreateInventoryDepositRequest.Item.builder()
//                                .unitPrice(3000.0)
//                                .productId(galaxyBuds2.getId())
//                                .quantity(100)
//                                .build()
//                ))
//                .build();
//
//        final var d = inventoryDepositService.deposit(testOrganization.getId(), deposit1);
//
//        inventoryDepositService.rollbackDeposit(testOrganization.getId(), d.getId());
//
//        final var searchRequest = InventoryDepositSearchRequest.builder()
//                .startDate(null)
//                .build();
//
//        System.out.println(inventoryDepositService.search(testOrganization.getId(), searchRequest, Pageable.of(1, 10)));
    }

}