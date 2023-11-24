package com.distrupify.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.entities.OrganizationEntity;
import com.distrupify.entities.ProductEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

@QuarkusTest
@TestHTTPEndpoint(PurchaseOrderResource.class)
@QuarkusTestResource(PostgresResource.class)
class PurchaseOrderResourceTest {
    private static final Logger LOGGER = Logger.getLogger(PurchaseOrderResourceTest.class);

    private static final String PASSWORD = "password";

    Long organizationId;

    String authHeader;

    @Inject
    AuthService authService;

//    @BeforeEach
//    @Transactional
//    public void beforeEach() {
//        LOGGER.info("==========TEST REGION==========");
//        final var organization = OrganizationEntity.builder()
//                .name("purchase-order-test-organization")
//                .displayName("Purchase Order Test Organization")
//                .build();
//        organization.persist();
//        organizationId = organization.getId();
//
//        final var signUpRequest = SignupRequest.builder()
//                    .email("new-user@email.com")
//                    .name("new test user")
//                    .password(PASSWORD)
//                    .organizationId(organizationId)
//                    .build();
//        final var tokenDTO = authService.signup(signUpRequest);
//        authHeader = "Bearer " + tokenDTO.token;
//
//        final var s22Ultra = ProductEntity.builder()
//                .organizationId(organization.getId())
//                .sku("1423333454124")
//                .brand("Samsung")
//                .name("Galaxy S22 Ultra")
//                .description("512GB Exclusive Gray")
//                .unitPrice(BigDecimal.valueOf(70000))
//                .build();
//        s22Ultra.persist();
//
//        final var galaxyBuds2 = ProductEntity.builder()
//                .organizationId(organization.getId())
//                .sku("123456789")
//                .brand("Samsung")
//                .name("Galaxy Buds 2")
//                .description("Onyx")
//                .unitPrice(BigDecimal.valueOf(4000))
//                .build();
//        galaxyBuds2.persist();
//    }
//
//    @AfterEach
//    @Transactional
//    public void afterEach() {
//        UserEntity.deleteAll();
//        ProductEntity.deleteAll();
//        OrganizationEntity.deleteAll();
//        LOGGER.info("==========END TEST REGION==========");
//    }
}