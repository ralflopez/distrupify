package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.resources.dto.ProductDTO;
import com.distrupify.models.ProductModel;
import com.distrupify.resources.requests.ProductCreateRequest;
import com.distrupify.resources.requests.ProductEditRequest;
import com.distrupify.resources.requests.ProductSearchFilterBy;
import com.distrupify.resources.response.ProductsResponse;
import com.distrupify.services.ProductService;
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

import java.util.List;

@Path("/api/v1/products")
@RequestScoped
public class ProductResource {
    private static final Logger LOGGER = Logger.getLogger(ProductResource.class);

    @Inject
    ProductService productService;

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    // TODO: write test
    @POST
    @Authenticated
    public Response createProduct(@Valid ProductCreateRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);

        LOGGER.infof("Creating product for organization %d " +
                        "{ sku: %s, brand: %s, name: %s, description: %s, unitPrice: %.2f }",
                organizationId, request.sku, request.brand,
                request.name, request.description, request.unitPrice);

        productService.create(organizationId, request);
        return Response.accepted().build();
    }

    // TODO: write test
    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteProduct(@PathParam("id") Long id) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        LOGGER.infof("Deleting product for organization %d { productId: %d }", organizationId, id);
        productService.softDelete(organizationId, id);
        return Response.accepted().build();
    }

    // TODO: write test
    @PUT
    @Path("/{id}")
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editProduct(@PathParam("id") Long id, @Valid ProductEditRequest request) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        LOGGER.infof("Editing product for organization %d { productId=%d }", organizationId, id);

        productService.edit(organizationId, id, request);
        return Response.accepted().build();
    }

    // TODO: write test
    @APIResponse(responseCode = "200",
            description = "Successful operation",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProductsResponse.class)))
    @GET
    @Authenticated
    public Response findProducts(@QueryParam("search") String searchStringParam,
                                 @QueryParam("filter_by") ProductSearchFilterBy filterByParam,
                                 @QueryParam("page") int page,
                                 @QueryParam("page_size") int pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var filterBy = ProductSearchFilterBy.getOrDefault(filterByParam);
        final var searchString = searchStringParam != null ? searchStringParam.trim() : "";
        final var pageable = Pageable.of(page, pageSize);

        LOGGER.infof("Searching products for organization %d " +
                        "{ searchString=%s, filterBy=%s, page=%d, pageSize=%s }",
                organizationId,
                searchString,
                filterBy,
                pageable.getPage(),
                pageable.getPageSize());

        List<ProductModel> products = switch (searchString) {
            case "" -> productService.findAll(organizationId, pageable);
            default -> productService.findAll(organizationId, pageable, searchString, filterBy);
        };

        final var pageCount = productService.getPageCount(organizationId, pageable.limit());
        final var response = new ProductsResponse(products.stream()
                .map(ProductDTO::fromModel)
                .toList(),
                pageCount);
        return Response.ok(response).build();
    }
}
