package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.exceptions.WebExceptionResponse;
import com.distrupify.resources.dto.InventoryAdjustmentDTO;
import com.distrupify.resources.requests.InventoryAdjustmentCreateRequest;
import com.distrupify.resources.response.InventoryAdjustmentResponse;
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
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
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

    @SuppressWarnings("unused")
    @POST
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createInventoryAdjustment(@Valid InventoryAdjustmentCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);

        LOGGER.infof("Creating inventory adjustment for organization %d { %s }",
                organizationId, request.toString());

        inventoryAdjustmentService.createInventoryAdjustment(organizationId, request);
        return Response.accepted().build();
    }

    // TODO: write test
    @SuppressWarnings("unused")
    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = InventoryAdjustmentResponse.class)))
    @GET
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findInventoryAdjustments(@QueryParam("page") Integer page, @QueryParam("page_size") Integer pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, pageSize);

        LOGGER.infof("Searching inventory adjustments for organization %d " +
                        "{ page=%d, pageSize=%s }",
                organizationId,
                pageable.getPage(),
                pageable.getPageSize());

        final var inventoryAdjustments = inventoryAdjustmentService.findAll(organizationId, pageable);
        final var pageCount = inventoryAdjustmentService.getPageCount(organizationId, pageable.limit());

        return Response.ok(new InventoryAdjustmentResponse(inventoryAdjustments.stream()
                        .map(InventoryAdjustmentDTO::fromEntity)
                        .toList(), pageCount))
                .build();
    }

    @GET
    @Path("transactions/{transactionId}")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByTransactionId(@PathParam("id") Long transactionId) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var inventoryAdjustment = inventoryAdjustmentService.findByTransactionId(organizationId, transactionId);

        if (inventoryAdjustment.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity(new WebExceptionResponse(
                    Response.Status.NOT_FOUND,
                    "Details not found"))
                    .build();
        }

        return Response.ok(InventoryAdjustmentDTO.fromEntity(inventoryAdjustment.get())).build();
    }
}
