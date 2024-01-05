package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.InventoryTransactionDTO;
import com.distrupify.requests.InventoryTransactionSearchRequest;
import com.distrupify.response.InventoryTransactionResponse;
import com.distrupify.services.InventoryTransactionService;
import com.distrupify.utils.Pageable;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.text.ParseException;
import java.util.Map;

@Path("/api/v1/inventory/transactions")
@RequestScoped
public class InventoryTransactionResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    TokenService tokenService;

    @Inject
    InventoryTransactionService inventoryTransactionService;

    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = InventoryTransactionResponse.class)))
    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("page") int page, @QueryParam("per_page") int perPage) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, perPage);
        final var transactions = inventoryTransactionService.findAll(organizationId, pageable);
        final var response = new InventoryTransactionResponse(transactions.stream()
                .map(InventoryTransactionDTO::fromEntity)
                .toList(), 0);
        return Response.ok(response).build();
    }

    @GET
    @Path("/search")
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(InventoryTransactionSearchRequest request,
                           @QueryParam("page") int page,
                           @QueryParam("per_page") int perPage,
                           @QueryParam("asc") boolean asc) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, perPage);
        try {
            final var transactions = inventoryTransactionService.searchAll(organizationId, request, pageable, asc);
            return Response.ok(Map.of("transactions",
                            transactions.stream()
                                    .map(InventoryTransactionDTO::fromEntity)
                                    .toList()))
                    .build();
        } catch (ParseException pe) {
            throw new InternalServerErrorException("Failed to parse date");
        }
    }

    // TODO: write test
    @SuppressWarnings("unused")
    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteInventoryTransaction(@PathParam("id") Long transactionId) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        inventoryTransactionService.softDelete(organizationId, transactionId);
        return Response.accepted().build();
    }
}
