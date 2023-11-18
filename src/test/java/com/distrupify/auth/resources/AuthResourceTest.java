package com.distrupify.auth.resources;

import com.distrupify.PostgresResource;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.LoginRequest;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.auth.services.TokenService;
import com.distrupify.auth.services.UserService;
import com.distrupify.entities.OrganizationEntity;
import com.distrupify.entities.ProductEntity;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.config.MatcherConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matcher;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;

// source: https://www.morling.dev/blog/quarkus-and-testcontainers/
// source: https://testcontainers.com/guides/development-and-testing-quarkus-application-using-testcontainers/

@QuarkusTest
@TestHTTPEndpoint(AuthResource.class)
@QuarkusTestResource(PostgresResource.class)
class AuthResourceTest {
    private static final Logger LOGGER = Logger.getLogger(AuthResource.class);
    
    private static final String PASSWORD = "password";

    Long organizationId;

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        LOGGER.info("==========TEST REGION==========");
        final var organization = OrganizationEntity.builder()
                .name("auth-test-organization")
                .displayName("Auth Test Organization")
                .build();
        organization.persist();
        organizationId = organization.getId();

        final var galaxyBuds2 = ProductEntity.builder()
                .organizationId(organization.getId())
                .sku("123456789")
                .brand("Samsung")
                .name("Galaxy Buds 2")
                .description("")
                .price(BigDecimal.valueOf(4000))
                .build();
        galaxyBuds2.persist();
    }

    @AfterEach
    @Transactional
    public void afterEach() {
        UserEntity.deleteAll();
        ProductEntity.deleteAll();
        OrganizationEntity.deleteAll();
        LOGGER.info("==========END TEST REGION==========");
    }

    @Test
    public void shouldSignUpNewUser() {
        final var signUpRequest = SignupRequest.builder()
                .email("new-user@emai.com")
                .name("new test user")
                .password(PASSWORD)
                .organizationId(organizationId)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when()
                .post("/signup")
                .then()
                .body("token", anything())
                .statusCode(200);
    }

    @Test
    public void shouldFailToSignUpExistingUser() {
        final var existingUser = createUser();

        final var signUpRequest = SignupRequest.builder()
                .email(existingUser.getEmail())
                .name("new test user")
                .password(PASSWORD)
                .organizationId(existingUser.getOrganizationId())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(signUpRequest)
                .when()
                .post("/signup")
                .then()
                .statusCode(400);
    }

    @Test
    public void shouldLogInExistingUser() {
        final var existingUser = createUser();

        final var loginRequest = LoginRequest.builder()
                .email(existingUser.getEmail())
                .password(PASSWORD)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .body("token", anything())
                .statusCode(200);
    }

    @Test
    public void shouldNotLogInNonExistentUser() {
        final var loginRequest = LoginRequest.builder()
                .email("invalid@email.com")
                .password(PASSWORD)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(400);
    }

    @Test
    public void shouldGetLoggedInUserDetails() {
        final var user = createUser();
        
        final var tokenDTO = tokenService.generateToken(organizationId, user.getEmail());

        given().headers("Authorization", "Bearer " + tokenDTO.token)
                .when()
                .get("/user")
                .then()
                .body("email", equalTo(user.getEmail()))
                .statusCode(200);
    }

    @Transactional
    public UserEntity createUser() {
        final var signUpRequest = SignupRequest.builder()
                .email("new-user@email.com")
                .name("new test user")
                .password(PASSWORD)
                .organizationId(organizationId)
                .build();
        authService.signup(signUpRequest);

        final var user = userService.findByEmail(organizationId, signUpRequest.getEmail());
        return user.orElse(null);
    }
}