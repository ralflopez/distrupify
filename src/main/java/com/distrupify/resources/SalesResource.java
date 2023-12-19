package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.SalesDTO;
import com.distrupify.requests.SalesCreateRequest;
import com.distrupify.response.SalesResponse;
import com.distrupify.services.SalesService;
import com.distrupify.utils.Pageable;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

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
    public Response findSales(@QueryParam("page") int page, @QueryParam("page_size") int pageSize) {
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
        return Response.ok().build();
    }
}
