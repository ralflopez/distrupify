package com.distrupify.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.*;
import com.distrupify.requests.InventoryTransactionSearchRequest;
import com.distrupify.utils.DateUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.stream.Stream;

import static com.distrupify.entities.InventoryTransactionEntity.Type.*;
import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(InventoryTransactionResource.class)
@QuarkusTestResource(PostgresResource.class)
class InventoryTransactionResourceTest {

    private static final String PASSWORD = "password";

    Long organizationId;

    String authHeader;

    ProductEntity s22Ultra;

    ProductEntity galaxyBuds2;

    SupplierEntity supplier;

    CustomerEntity customer;

    @Inject
    AuthService authService;

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
        InventoryWithdrawalEntity.deleteAll();
        PurchaseOrderEntity.deleteAll();
        InventoryAdjustmentEntity.deleteAll();
        InventoryLogEntity.deleteAll();
        InventoryTransactionEntity.deleteAll();
        ProductEntity.deleteAll();
        UserEntity.deleteAll();
        SupplierEntity.deleteAll();
        CustomerEntity.deleteAll();
        OrganizationEntity.deleteAll();
    }

    @Transactional
    public void _shouldGetAllTransactionTypesData() {
        final var t1 = new InventoryAdjustmentEntity(organizationId);
        t1.addLog(InventoryLogEntity.Type.INCOMING, s22Ultra.getId(), 100);
        t1.addLog(InventoryLogEntity.Type.INCOMING, galaxyBuds2.getId(), 100);
        t1.persist();

        final var t2 = new InventoryWithdrawalEntity(organizationId, customer.getId());
        t2.addLog(s22Ultra.getId(), 10, 100);
        t2.addLog(galaxyBuds2.getId(), 10, 50);
        t2.persist();

        final var t3 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t3.addLog(s22Ultra.getId(), 50, 100);
        t3.addLog(galaxyBuds2.getId(), 50, 50);
        t3.persist();

        final var t4 = new InventoryAdjustmentEntity(organizationId);
        t4.addLog(InventoryLogEntity.Type.OUTGOING, s22Ultra.getId(), 30);
        t4.addLog(InventoryLogEntity.Type.OUTGOING, galaxyBuds2.getId(), 10);
        t4.persist();
    }

    @Test
    public void shouldGetAllTransactionTypes() {
        _shouldGetAllTransactionTypesData();
        given()
                .contentType(ContentType.JSON)
                .headers("Authorization", authHeader)
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .body("transactions", Matchers.hasSize(4))
                .body("transactions[0].inventoryTransactionType", Matchers.is(ADJUSTMENT.name()))
                .body("transactions[1].inventoryTransactionType", Matchers.is(PURCHASE_ORDER.name()))
                .body("transactions[2].inventoryTransactionType", Matchers.is(WITHDRAWAL.name()))
                .body("transactions[3].inventoryTransactionType", Matchers.is(ADJUSTMENT.name()));
    }

    private static Stream<Arguments> shouldSearchBetweenDatesParameters() throws ParseException {
        return Stream.of(
                Arguments.of("2012-12-22", "2013-04-24", 6),
                Arguments.of("2012-12-22", null, 7),
                Arguments.of(null, null, 8)
        );
    }

    @Transactional
    public void _shouldSearchBetweenDatesData() throws ParseException {
        final var t1 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t1.addLog(s22Ultra.getId(), 50, 100);
        t1.setCreatedAt(DateUtil.from("2012-11-22"));
        t1.persist();

        final var t2 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t2.addLog(s22Ultra.getId(), 50, 100);
        t2.setCreatedAt(DateUtil.from("2012-12-22"));
        t2.persist();

        final var t3 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t3.addLog(s22Ultra.getId(), 50, 100);
        t3.setCreatedAt(DateUtil.from("2012-12-25"));
        t3.persist();

        final var t4 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t4.addLog(s22Ultra.getId(), 50, 100);
        t4.setCreatedAt(DateUtil.from("2013-01-01"));
        t4.persist();

        final var t5 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t5.addLog(s22Ultra.getId(), 50, 100);
        t5.setCreatedAt(DateUtil.from("2013-02-24"));
        t5.persist();

        final var t6 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t6.addLog(s22Ultra.getId(), 50, 100);
        t6.setCreatedAt(DateUtil.from("2013-03-24"));
        t6.persist();

        final var t7 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t7.addLog(s22Ultra.getId(), 50, 100);
        t7.setCreatedAt(DateUtil.from("2013-04-24"));
        t7.persist();

        final var t8 = new PurchaseOrderEntity(organizationId, supplier.getId());
        t8.addLog(s22Ultra.getId(), 50, 100);
        t8.setCreatedAt(DateUtil.from("2013-05-24"));
        t8.persist();
    }

    @ParameterizedTest
    @MethodSource("shouldSearchBetweenDatesParameters")
    public void shouldSearchBetweenDates(String startDate, String endDate, int count) throws ParseException {
        _shouldSearchBetweenDatesData();
        final var request = InventoryTransactionSearchRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .build();

        given()
                .contentType(ContentType.JSON)
                .log().all()
                .headers("Authorization", authHeader)
                .body(request)
                .when()
                .get("/search?asc")
                .then()
                .log().all()
                .statusCode(200)
                .body("transactions", Matchers.hasSize(count));
    }
}