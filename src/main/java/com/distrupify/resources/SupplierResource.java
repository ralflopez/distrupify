package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.SupplierDTO;
import com.distrupify.requests.SupplierCreateRequest;
import com.distrupify.requests.SupplierEditRequest;
import com.distrupify.response.SuppliersResponse;
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
    @GET
    @Authenticated
    public Response findSuppliers(@QueryParam("page") int page,
                                  @QueryParam("page_size") int pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, pageSize);

        LOGGER.infof("Searching suppliers for organization %d { page=%d, pageSize=%s }",
                organizationId,
                pageable.getPage(),
                pageable.getPageSize());

        final var suppliers = supplierService.findAll(organizationId, pageable);

        final var pageCount = supplierService.getPageCount(organizationId, pageable.limit());
        final var response = new SuppliersResponse(suppliers.stream()
                .map(SupplierDTO::fromEntity)
                .toList(),
                pageCount);
        return Response.ok(response).build();
    }
}