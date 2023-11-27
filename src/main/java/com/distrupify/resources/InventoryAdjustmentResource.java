package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.requests.InventoryAdjustmentCreateRequest;
import com.distrupify.services.InventoryAdjustmentService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1/inventory/adjustments")
@RequestScoped
public class InventoryAdjustmentResource {
    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @Inject
    InventoryAdjustmentService inventoryAdjustmentService;

    @POST
    @Authenticated
    @SuppressWarnings("unused")
    public Response createInventoryAdjustment(@Valid InventoryAdjustmentCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        inventoryAdjustmentService.createInventoryAdjustment(organizationId, request);
        return Response.ok().build();
    }
}
