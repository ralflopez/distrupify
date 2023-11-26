package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.requests.PurchaseOrderCreateRequest;
import com.distrupify.services.InventoryTransactionService;
import com.distrupify.services.PurchaseOrderService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.logging.Logger;

@Path("/api/v1/purchase-order")
@RequestScoped
public class PurchaseOrderResource {
    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @Inject
    PurchaseOrderService purchaseOrderService;

    @POST
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPurchaseOrder(@Valid PurchaseOrderCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        purchaseOrderService.createPurchaseOrder(organizationId, request);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}")
    @Authenticated
    public Response receivePurchaseOrder(@PathParam("id") Long id) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        purchaseOrderService.receive(organizationId, id);
        return Response.ok().build();
    }
}
