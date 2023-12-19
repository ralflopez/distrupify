package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.InventoryAdjustmentDTO;
import com.distrupify.requests.InventoryAdjustmentCreateRequest;
import com.distrupify.response.InventoryAdjustmentResponse;
import com.distrupify.services.InventoryAdjustmentService;
import com.distrupify.utils.Pageable;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@Path("/api/v1/inventory/adjustments")
@RequestScoped
public class InventoryAdjustmentResource {
    private static final Logger LOGGER = Logger.getLogger(InventoryAdjustmentResource.class);

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @Inject
    InventoryAdjustmentService inventoryAdjustmentService;

    @POST
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createInventoryAdjustment(@Valid InventoryAdjustmentCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        inventoryAdjustmentService.createInventoryAdjustment(organizationId, request);
        return Response.ok().build();
    }

    // TODO: write test
    // TODO: consistent naming convention for getAll / findAll
    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllInventoryAdjustment(@QueryParam("page") int page, @QueryParam("page_size") int pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, pageSize);
        final var inventoryAdjustments = inventoryAdjustmentService.findAll(organizationId, pageable);
        final var pageCount = inventoryAdjustmentService.getPageCount(organizationId, pageable.limit());

        return Response.ok(new InventoryAdjustmentResponse(inventoryAdjustments.stream()
                        .map(InventoryAdjustmentDTO::fromEntity)
                        .toList(), pageCount))
                .build();
    }
}
