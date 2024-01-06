package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.resources.dto.InventoryTransactionDTO;
import com.distrupify.entities.InventoryTransactionEntity.InventoryTransactionStatus;
import com.distrupify.entities.InventoryTransactionEntity.InventoryTransactionType;
import com.distrupify.exceptions.WebException;
import com.distrupify.resources.requests.InventoryTransactionSearchRequest;
import com.distrupify.resources.response.InventoryTransactionResponse;
import com.distrupify.services.InventoryTransactionService;
import com.distrupify.utils.DateUtil;
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
import org.jboss.logging.Logger;

import java.text.ParseException;
import java.util.List;

@Path("/api/v1/inventory/transactions")
@RequestScoped
public class InventoryTransactionResource {

    private static final Logger LOGGER = Logger.getLogger(InventoryTransactionResource.class);

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
    @Path("/all")
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

    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = InventoryTransactionResponse.class)))
    @GET
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findTransactions(@QueryParam("start") String start,
                                     @QueryParam("end") String end,
                                     @QueryParam("type") List<InventoryTransactionType> type,
                                     @QueryParam("status") List<InventoryTransactionStatus> status,
                                     @QueryParam("page") int page,
                                     @QueryParam("per_page") int perPage,
                                     @QueryParam("asc") boolean asc) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, perPage);
        try {
            final var request = new InventoryTransactionSearchRequest(start, end, type, status);
            final var transactions = inventoryTransactionService.find(organizationId, request, pageable, asc);
            final var response = new InventoryTransactionResponse(transactions, 0);
            return Response.ok(response).build();
        } catch (ParseException pe) {
            throw new WebException.InternalServerError("Failed to get the date. Make sure the format is " + DateUtil.DEFAULT_DATE_FORMAT);
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
