package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.requests.InventoryWithdrawalCreateRequest;
import com.distrupify.services.InventoryWithdrawalService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1/inventory/withdrawal")
@RequestScoped
public class InventoryWithdrawalResource {
    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @Inject
    InventoryWithdrawalService inventoryWithdrawalService;

    @POST
    @Authenticated
    @SuppressWarnings("unused")
    public Response createInventoryWithdrawal(@Valid InventoryWithdrawalCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        inventoryWithdrawalService.createInventoryWithdrawal(organizationId, request);
        return Response.ok().build();
    }
}
