package com.distrupify.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.*;
import com.distrupify.models.InventoryLogModel;
import com.distrupify.models.InventoryTransactionModel;
import com.distrupify.repository.InventoryTransactionRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(ProductResource.class)
@QuarkusTestResource(PostgresResource.class)
class ProductResourceTest {

    private static final String PASSWORD = "password";

    Long organizationId;

    String authHeader;

    ProductEntity s22Ultra;

    ProductEntity galaxyBuds2;

    @Inject
    AuthService authService;

    @Inject
    InventoryTransactionRepository inventoryTransactionRepository;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        final var organization = OrganizationEntity.builder()
                .name("products-test-organization")
                .displayName("Products Organization")
                .build();
        organization.persist();
        organizationId = organization.getId();

        final var signUpRequest = SignupRequest.builder()
                .email("new-user@email.com")
                .name("new test user")
                .password(PASSWORD)
                .organizationId(organizationId)
                .build();
        final var tokenDTO = authService.signup(signUpRequest);
        authHeader = "Bearer " + tokenDTO.token;

        s22Ultra = ProductEntity.builder()
                .organizationId(organization.getId())
                .sku("1423333454124")
                .brand("Samsung")
                .name("Galaxy S22 Ultra")
                .description("512GB Exclusive Gray")
                .unitPrice(BigDecimal.valueOf(70000))
                .build();
        s22Ultra.persist();

        galaxyBuds2 = ProductEntity.builder()
                .organizationId(organization.getId())
                .sku("123456789")
                .brand("Samsung")
                .name("Galaxy Buds 2")
                .description("Onyx")
                .unitPrice(BigDecimal.valueOf(4000))
                .build();
        galaxyBuds2.persist();
    }

    @AfterEach
    @Transactional
    public void afterEach() {
        PurchaseOrderEntity.deleteAll();
        InventoryWithdrawEntity.deleteAll();
        InventoryDepositEntity.deleteAll();
        InventoryLogEntity.deleteAll();
        InventoryTransactionEntity.deleteAll();
        ProductEntity.deleteAll();
        UserEntity.deleteAll();
        OrganizationEntity.deleteAll();
    }

    @Test
    public void shouldGetTheCorrectProductQuantity() {
        shouldGetTheCorrectProductQuantityData();

        given()
                .headers("Authorization", authHeader)
                .when()
                .get()
                .then()
                .body("products", Matchers.hasSize(2))
                .body("products[0].quantity", Matchers.equalTo(119)) // galaxy buds
                .body("products[1].quantity", Matchers.equalTo(32)) // s22 ultra
                .statusCode(200);
    }

    @Transactional
    public void shouldGetTheCorrectProductQuantityData() {
        final var t1 = InventoryTransactionModel.createInventoryDeposit(organizationId);
        t1.addLog(InventoryLogModel.Type.INCOMING, 13, 0, s22Ultra.getId());
        t1.addLog(InventoryLogModel.Type.INCOMING, 56, 0, galaxyBuds2.getId());
        inventoryTransactionRepository.persist(t1);

        final var t2 = InventoryTransactionModel.createInventoryWithdraw(organizationId);
        t2.addLog(InventoryLogModel.Type.OUTGOING, 5, 0, s22Ultra.getId());
        t2.addLog(InventoryLogModel.Type.OUTGOING, 4, 0, galaxyBuds2.getId());
        inventoryTransactionRepository.persist(t2);

        final var t3 = InventoryTransactionModel.createPurchaseOrder(organizationId);
        t3.addLog(InventoryLogModel.Type.INCOMING, 200, 0, s22Ultra.getId());
        t3.addLog(InventoryLogModel.Type.INCOMING, 100, 0, galaxyBuds2.getId());
        inventoryTransactionRepository.persist(t3);

        final var t4 = InventoryTransactionModel.createInventoryWithdraw(organizationId);
        t4.addLog(InventoryLogModel.Type.OUTGOING, 1, 0, s22Ultra.getId());
        t4.addLog(InventoryLogModel.Type.OUTGOING, 3, 0, galaxyBuds2.getId());
        inventoryTransactionRepository.persist(t4);

        final var t5 = InventoryTransactionModel.createInventoryDeposit(organizationId);
        t5.addLog(InventoryLogModel.Type.INCOMING, 20, 0, s22Ultra.getId());
        t5.addLog(InventoryLogModel.Type.INCOMING, 44, 0, galaxyBuds2.getId());
        inventoryTransactionRepository.persist(t5);

        final var t6 = InventoryTransactionModel.createCompletedPurchaseOrder(organizationId);
        t6.addLog(InventoryLogModel.Type.INCOMING, 5, 0, s22Ultra.getId());
        t6.addLog(InventoryLogModel.Type.INCOMING, 26, 0, galaxyBuds2.getId());
        inventoryTransactionRepository.persist(t6);
    }

}