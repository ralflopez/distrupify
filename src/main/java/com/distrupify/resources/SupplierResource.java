package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.resources.dto.SupplierDTO;
import com.distrupify.resources.requests.SupplierCreateRequest;
import com.distrupify.resources.requests.SupplierEditRequest;
import com.distrupify.resources.response.SalesResponse;
import com.distrupify.resources.response.SuppliersResponse;
import com.distrupify.services.SupplierService;
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

@Path("/api/v1/suppliers")
@RequestScoped
public class SupplierResource {
    private static final Logger LOGGER = Logger.getLogger(SupplierResource.class);

    @Inject
    SupplierService supplierService;

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    // TODO: write test
    @POST
    @Authenticated
    public Response createSupplier(@Valid SupplierCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);

        LOGGER.infof("Creating supplier for organization %d { %s }", organizationId, request);

        supplierService.create(organizationId, request);
        return Response.accepted().build();
    }

    // TODO: write test
    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteSupplier(@PathParam("id") Long id) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        LOGGER.infof("Deleting supplier for organization %d { productId=%d }", organizationId, id);
        supplierService.softDelete(organizationId, id);
        return Response.accepted().build();
    }

    // TODO: write test
    @PUT
    @Path("/{id}")
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editProduct(@PathParam("id") Long id, @Valid SupplierEditRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        LOGGER.infof("Editing supplier for organization %d { productId=%d }", organizationId, id);

        supplierService.edit(organizationId, id, request);
        return Response.accepted().build();
    }

    // TODO: write test
    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SuppliersResponse.class)))
    @GET
    @Authenticated
    public Response findSuppliers(@QueryParam("page") Integer page,
                                  @QueryParam("page_size") Integer pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, pageSize);

        final var suppliers = supplierService.findAll(organizationId, pageable);
        final var pageCount = supplierService.getPageCount(organizationId, pageable);

        final var response = new SuppliersResponse(suppliers.stream()
                .map(SupplierDTO::fromEntity)
                .toList(),
                pageCount);
        return Response.ok(response).build();
    }
}
