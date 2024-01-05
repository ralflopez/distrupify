package com.distrupify.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.*;
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

    ProductEntity headRadicalMP;

    SupplierEntity supplier;

    CustomerEntity customer;

    @Inject
    AuthService authService;

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

        supplier = SupplierEntity.builder()
                .address("Manila")
                .contactNumber("0927")
                .name("Default")
                .organizationId(organizationId)
                .build();
        supplier.persist();

        customer = new CustomerEntity(organizationId, "Customer", "Manila", "0917");
        customer.persist();

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

        headRadicalMP = ProductEntity.builder()
                .organizationId(organization.getId())
                .sku("371836472")
                .brand("Head")
                .name("Radical MP")
                .description("Flexpoint")
                .unitPrice(BigDecimal.valueOf(15000))
                .build();
        headRadicalMP.persist();
    }

    @AfterEach
    @Transactional
    public void afterEach() {
        PurchaseOrderEntity.deleteAll();
        InventoryAdjustmentEntity.deleteAll();
        SalesEntity.deleteAll();
        InventoryDepositEntity.deleteAll();
        InventoryLogEntity.deleteAll();
        InventoryTransactionEntity.deleteAll();
        ProductEntity.deleteAll();
        UserEntity.deleteAll();
        SupplierEntity.deleteAll();
        CustomerEntity.deleteAll();
        OrganizationEntity.deleteAll();
    }

    @Test
    public void shouldGetTheCorrectProductQuantity() {
        _shouldGetTheCorrectProductQuantityData();

        given()
                .headers("Authorization", authHeader)
                .when()
                .get()
                .then()
                .body("products", Matchers.hasSize(3))
                .body("products[0].quantity", Matchers.equalTo(0)) // head radical mp
                .body("products[1].quantity", Matchers.equalTo(119)) // galaxy buds
                .body("products[2].quantity", Matchers.equalTo(32)) // s22 ultra
                .body("pageCount", Matchers.is(1))
                .statusCode(200);
    }

    @Transactional
    public void _shouldGetTheCorrectProductQuantityData() {
        final var t1 = new InventoryAdjustmentEntity(organizationId);
        t1.addLog(InventoryLogEntity.InventoryLogType.INCOMING, s22Ultra.getId(), 13);
        t1.addLog(InventoryLogEntity.InventoryLogType.INCOMING, galaxyBuds2.getId(), 56);
        t1.persist();

        final var t2 = new SalesEntity(organizationId, customer.getId());
        t2.addLog(s22Ultra.getId(), 5, 0);
        t2.addLog(galaxyBuds2.getId(), 4, 0);
        t2.persist();

        final var t3 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t3.addLog(s22Ultra.getId(), 200, 0);
        t3.addLog(galaxyBuds2.getId(), 100, 0);
        t3.persist();

        final var t4 = new SalesEntity(organizationId, customer.getId());
        t4.addLog(s22Ultra.getId(), 1, 0);
        t4.addLog(galaxyBuds2.getId(), 3, 0);
        t4.persist();

        final var t5 = new InventoryAdjustmentEntity(organizationId);
        t5.addLog(InventoryLogEntity.InventoryLogType.INCOMING, s22Ultra.getId(), 20);
        t5.addLog(InventoryLogEntity.InventoryLogType.INCOMING, galaxyBuds2.getId(), 44);
        t5.persist();

        final var t6 = new PurchaseOrderEntity(organizationId, supplier.getId(),false);
        t6.addLog(s22Ultra.getId(), 5, 0);
        t6.addLog(galaxyBuds2.getId(), 26, 0);
        t6.persist();
    }

}