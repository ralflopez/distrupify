package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.InventoryTransactionDTO;
import com.distrupify.requests.InventoryTransactionSearchRequest;
import com.distrupify.services.InventoryTransactionService;
import com.distrupify.utils.Pageable;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.text.ParseException;
import java.util.Map;

import static com.distrupify.utils.Pageable.DEFAULT_PAGE_SIZE;

@Path("/api/v1/inventory/transactions")
@RequestScoped
public class InventoryTransactionResource {
    @Inject
    JsonWebToken jwt;

    @Inject
    TokenService tokenService;

    @Inject
    InventoryTransactionService inventoryTransactionService;

    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, perPage);
        final var transactions = inventoryTransactionService.findAll(organizationId, pageable);
        return Response.ok(Map.of("transactions",
                        transactions.stream()
                                .map(InventoryTransactionDTO::fromEntity)
                                .toList()))
                .build();
    }

    @GET
    @Path("/search")
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(InventoryTransactionSearchRequest request,
                           @QueryParam("page") Integer page,
                           @QueryParam("per_page") Integer perPage,
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
}
