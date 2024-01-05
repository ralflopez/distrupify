package com.distrupify.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.*;
import com.distrupify.requests.PurchaseOrderCreateRequest;
import com.distrupify.services.ProductService;
import com.distrupify.utils.DependsOn;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.distrupify.entities.InventoryLogEntity.InventoryLogType.INCOMING;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PurchaseOrderResource.class)
@QuarkusTestResource(PostgresResource.class)
class PurchaseOrderResourceTest {
    private static final String PASSWORD = "password";

    Long organizationId;

    String authHeader;

    ProductEntity s22Ultra;

    ProductEntity galaxyBuds2;

    SupplierEntity supplier;

    @Inject
    AuthService authService;

    @Inject
    ProductService productService;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        final var organization = OrganizationEntity.builder()
                .name("purchase-order-test-organization")
                .displayName("Purchase Order Organization")
                .build();
        organization.persist();
        organizationId = organization.getId();

        supplier = SupplierEntity.builder()
                .address("Manila")
                .contactNumber("0927")
                .name("Default")
                .organizationId(organizationId)
                .build();
        supplier.persist();

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
        SalesEntity.deleteAll();
        InventoryDepositEntity.deleteAll();
        InventoryLogEntity.deleteAll();
        InventoryTransactionEntity.deleteAll();
        ProductEntity.deleteAll();
        UserEntity.deleteAll();
        SupplierEntity.deleteAll();
        OrganizationEntity.deleteAll();
    }

    @Test
    public void shouldAddTheLogsAfterCreatingAPurchaseOrder() {
        final var request = PurchaseOrderCreateRequest.builder()
                .items(List.of(PurchaseOrderCreateRequest.PurchaseOrderCreateRequestItem.builder()
                                .productId(galaxyBuds2.getId())
                                .quantity(150)
                                .unitPrice(0.0)
                                .build(),
                        PurchaseOrderCreateRequest.PurchaseOrderCreateRequestItem.builder()
                                .productId(s22Ultra.getId())
                                .quantity(80)
                                .unitPrice(0.0)
                                .build()))
                .supplierId(supplier.getId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", authHeader)
                .body(request)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200);

        final var logs = InventoryLogEntity.findAll()
                .stream()
                .map(e -> (InventoryLogEntity) e)
                .toList();
        assertEquals(2, logs.size());
        assertTrue(logs.stream().allMatch(l -> l.getInventoryLogType().equals(INCOMING)));
    }

    @Test
    @DependsOn(value = {"ProductResourceTest.shouldGetTheCorrectProductQuantity"})
    public void shouldOnlyUpdateProductCountAfterReceivingThePurchaseOrder() {
        // Create
        final var GALAXY_BUDS_2_COUNT = 150;
        final var S22_ULTRA_COUNT = 80;

        final var request = PurchaseOrderCreateRequest.builder()
                .items(List.of(PurchaseOrderCreateRequest.PurchaseOrderCreateRequestItem.builder()
                                .productId(galaxyBuds2.getId())
                                .quantity(GALAXY_BUDS_2_COUNT)
                                .unitPrice(0.0)
                                .build(),
                        PurchaseOrderCreateRequest.PurchaseOrderCreateRequestItem.builder()
                                .productId(s22Ultra.getId())
                                .quantity(S22_ULTRA_COUNT)
                                .unitPrice(0.0)
                                .build()))
                .supplierId(supplier.getId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", authHeader)
                .body(request)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(200);

        var products = productService.findAll(organizationId);
        var s22UltraEntry = products.stream()
                .filter(p -> p.id().orElse(-1L).equals(s22Ultra.getId()))
                .findFirst();
        assertTrue(s22UltraEntry.isPresent());
        assertEquals(0, s22UltraEntry.get().quantity());

        var galaxyBuds2Entry = products.stream()
                .filter(p -> p.id().orElse(-1L).equals(galaxyBuds2.getId()))
                .findFirst();
        assertTrue(galaxyBuds2Entry.isPresent());
        assertEquals(0, galaxyBuds2Entry.get().quantity());

        // Receive
        final var transactions = PurchaseOrderEntity.findAll().stream().toList();
        assertEquals(1, transactions.size());
        final var transaction = (PurchaseOrderEntity) transactions.get(0);

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", authHeader)
                .body(request)
                .when()
                .post("/" + transaction.getId())
                .then()
                .log().all()
                .statusCode(200);

        products = productService.findAll(organizationId);
        s22UltraEntry = products.stream()
                .filter(p -> p.id().orElse(-1L).equals(s22Ultra.getId()))
                .findFirst();
        assertTrue(s22UltraEntry.isPresent());
        assertEquals(S22_ULTRA_COUNT, s22UltraEntry.get().quantity());

        galaxyBuds2Entry = products.stream()
                .filter(p -> p.id().orElse(-1L).equals(galaxyBuds2.getId()))
                .findFirst();
        assertTrue(galaxyBuds2Entry.isPresent());
        assertEquals(GALAXY_BUDS_2_COUNT, galaxyBuds2Entry.get().quantity());
    }
}