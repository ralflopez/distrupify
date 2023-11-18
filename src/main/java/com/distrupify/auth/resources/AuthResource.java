package com.distrupify.auth.resources;

import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.LoginRequest;
import com.distrupify.auth.requests.SignupRequest;
import com.distrupify.auth.services.AuthService;
import com.distrupify.auth.services.TokenService;
import com.distrupify.auth.services.UserService;
import com.distrupify.entities.OrganizationEntity;
import com.distrupify.validations.EntityFieldsCombinationValidator;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@Path("/api/v1/auth")
@RequestScoped
public class AuthResource {
    private static final Logger LOGGER = Logger.getLogger(AuthResource.class);

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @Inject
    JsonWebToken jwt;

    @Inject
    TokenService tokenService;

    @Inject
    EntityFieldsCombinationValidator entityFieldsCombinationValidator;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequest loginRequest) {
        final var tokenDTO = authService.login(loginRequest);
        return Response.ok(tokenDTO).build();
    }

    @POST
    @Path("/signup")
    @PermitAll
    public Response signup(@Valid SignupRequest signupRequest) {
        entityFieldsCombinationValidator.checkUniqueEmailAndOrganizationOrThrow(signupRequest.getOrganizationId(),
                signupRequest.getEmail());

        final var tokenDTO = authService.signup(signupRequest);
        return Response.ok(tokenDTO).build();
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getUser(@Context SecurityContext ctx) {
        final var principal = ctx.getUserPrincipal();
        final var organizationId = tokenService.getOrganizationId(jwt);

        final var user = userService.findByEmail(organizationId, principal.getName())
                .orElseThrow(() -> new NotFoundException("User details not found"));
        return Response.ok(user.intoDTO()).build();
    }
}
