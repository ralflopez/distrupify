package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.exceptions.WebExceptionResponse;
import com.distrupify.resources.dto.PurchaseOrderDTO;
import com.distrupify.resources.requests.PurchaseOrderCreateRequest;
import com.distrupify.services.PurchaseOrderService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/api/v1/purchase-orders")
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
    @SuppressWarnings("unused")
    public Response create(@Valid PurchaseOrderCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        purchaseOrderService.create(organizationId, request);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/receive")
    @Authenticated
    @SuppressWarnings("unused")
    public Response receive(@PathParam("id") Long id) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        purchaseOrderService.receive(organizationId, id);
        return Response.ok().build();
    }

    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = PurchaseOrderDTO.class)))
    @GET
    @Path("transactions/{transactionId}")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByTransactionId(@PathParam("transactionId") Long transactionId) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var purchaseOrder = purchaseOrderService.findByTransactionId(organizationId, transactionId);

        if (purchaseOrder.isEmpty()) {
            return Response.status(NOT_FOUND).entity(new WebExceptionResponse(
                            NOT_FOUND,
                            "Details not found"))
                    .build();
        }

        return Response.ok(PurchaseOrderDTO.fromEntity(purchaseOrder.get())).build();
    }
}
