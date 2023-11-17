package com.distrupify.controllers;

import com.distrupify.auth.services.TokenService;
import com.distrupify.repository.InventoryTransactionRepository;
import com.distrupify.requests.InventoryDepositCreateRequest;
import com.distrupify.services.InventoryTransactionService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1/inventory/deposit")
@RequestScoped
public class InventoryDepositRestController {

    @Inject
    InventoryTransactionService inventoryTransactionService;

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response deposit(@Valid InventoryDepositCreateRequest createRequest) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        inventoryTransactionService.persist(createRequest.intoModel(organizationId));
        return Response.ok().build();
    }
}
