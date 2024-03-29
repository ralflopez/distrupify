package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.exceptions.WebExceptionResponse;
import com.distrupify.resources.dto.PurchaseOrderDTO;
import com.distrupify.resources.dto.SalesDTO;
import com.distrupify.resources.requests.SalesCreateRequest;
import com.distrupify.resources.response.SalesResponse;
import com.distrupify.services.SalesService;
import com.distrupify.utils.Pageable;
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

@Path("/api/v1/sales")
@RequestScoped
public class SalesResource {
    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @Inject
    SalesService salesService;

    // TODO: write test
    @SuppressWarnings("unused")
    @GET
    @Authenticated
    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SalesResponse.class)))
    public Response findSales(@QueryParam("page") Integer page, @QueryParam("page_size") Integer pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, pageSize);
        final var sales = salesService.findAll(organizationId, pageable);
        final var pageCount = salesService.getPageCount(organizationId, pageable.getPageSize());
        final var response = new SalesResponse(sales.stream()
                .map(SalesDTO::fromEntity)
                .toList(),
                pageCount);
        return Response.ok(response).build();
    }

    // TODO: write test
    @SuppressWarnings("unused")
    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteSales(@PathParam("id") Long salesId) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        salesService.softDelete(organizationId, salesId);
        return Response.accepted().build();
    }

    // TODO: write test
    @SuppressWarnings("unused")
    @POST
    @Authenticated
    public Response createSales(@Valid SalesCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        salesService.create(organizationId, request);
        return Response.accepted().build();
    }

    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SalesDTO.class)))
    @GET
    @Path("transactions/{transactionId}")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByTransactionId(@PathParam("transactionId") Long transactionId) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var sales = salesService.findByTransactionId(organizationId, transactionId);

        if (sales.isEmpty()) {
            return Response.status(NOT_FOUND).entity(new WebExceptionResponse(
                            NOT_FOUND,
                            "Details not found"))
                    .build();
        }

        return Response.ok(SalesDTO.fromEntity(sales.get())).build();
    }
}
