package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.requests.PurchaseOrderCreateRequest;
import com.distrupify.services.InventoryTransactionService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1/purchase-order")
@RequestScoped
public class PurchaseOrderResource {
    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @Inject
    InventoryTransactionService inventoryTransactionService;

    @POST
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPurchaseOrder(@Valid PurchaseOrderCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        inventoryTransactionService.persist(request.intoModel(organizationId));
        return Response.ok().build();
    }
}
