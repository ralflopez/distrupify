package com.distrupify.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.*;
import com.distrupify.requests.SalesCreateRequest;
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

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(SalesResource.class)
@QuarkusTestResource(PostgresResource.class)
class InventoryWithdrawalResourceTest {
    private static final String PASSWORD = "password";

    Long organizationId;

    String authHeader;

    ProductEntity s22Ultra;

    ProductEntity galaxyBuds2;

    CustomerEntity customer;

    @Inject
    AuthService authService;

    @Inject
    ProductService productService;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        final var organization = OrganizationEntity.builder()
                .name("inventory-withdraw-test-organization")
                .displayName("Inventory Withdraw Organization")
                .build();
        organization.persist();
        organizationId = organization.getId();

        customer = new CustomerEntity(organizationId, "Customer", "Manila", "0917");
        customer.persist();

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
        SalesEntity.deleteAll();
        InventoryAdjustmentEntity.deleteAll();
        InventoryLogEntity.deleteAll();
        InventoryTransactionEntity.deleteAll();
        ProductEntity.deleteAll();
        UserEntity.deleteAll();
        CustomerEntity.deleteAll();
        OrganizationEntity.deleteAll();
    }

    @Transactional
    public void _shouldUpdateTheProductQuantityData() {
        final var t1 = new InventoryAdjustmentEntity(organizationId);
        t1.addLog(InventoryLogEntity.InventoryLogType.INCOMING, s22Ultra.getId(), 186, 0);
        t1.addLog(InventoryLogEntity.InventoryLogType.INCOMING, galaxyBuds2.getId(), 250, 0);
        t1.persist();
    }

    @Test
    @DependsOn(value = {"ProductResourceTest.shouldGetTheCorrectProductQuantity"})
    public void shouldUpdateTheProductQuantity() {
        _shouldUpdateTheProductQuantityData();

        final var request = SalesCreateRequest.builder()
                .items(List.of(
                        new SalesCreateRequest.SalesCreateRequestItem(s22Ultra.getId(), 100, 0.0),
                        new SalesCreateRequest.SalesCreateRequestItem(galaxyBuds2.getId(), 200, 0.0)
                ))
                .customerId(customer.getId())
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
        assertEquals(86, s22UltraEntry.get().quantity());

        var galaxyBuds2Entry = products.stream()
                .filter(p -> p.id().orElse(-1L).equals(galaxyBuds2.getId()))
                .findFirst();
        assertTrue(galaxyBuds2Entry.isPresent());
        assertEquals(50, galaxyBuds2Entry.get().quantity());
    }

    @Test
    public void shouldFailWhenNotEnoughQuantity() {
        _shouldUpdateTheProductQuantityData();

        final var request = SalesCreateRequest.builder()
                .items(List.of(
                        new SalesCreateRequest.SalesCreateRequestItem(s22Ultra.getId(), 9999, 0.0),
                        new SalesCreateRequest.SalesCreateRequestItem(galaxyBuds2.getId(), 100, 0.0)
                ))
                .customerId(customer.getId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", authHeader)
                .body(request)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void shouldFailWhenProductIdIsInvalid() {
        final var request = SalesCreateRequest.builder()
                .items(List.of(
                        new SalesCreateRequest.SalesCreateRequestItem(0L, 100, 0.0),
                        new SalesCreateRequest.SalesCreateRequestItem(galaxyBuds2.getId(), 100, 0.0)
                ))
                .customerId(customer.getId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", authHeader)
                .body(request)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(400);
    }
}