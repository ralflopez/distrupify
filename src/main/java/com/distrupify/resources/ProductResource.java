package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.ProductDTO;
import com.distrupify.response.ProductsResponse;
import com.distrupify.services.ProductService;
import com.distrupify.utils.Pageable;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1/products")
@RequestScoped
public class ProductResource {
    @Inject
    ProductService productService;

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Authenticated
    public Response getProducts(@QueryParam("page") int page, @QueryParam("page_size") int pageSize) {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var pageable = Pageable.of(page, pageSize);
        final var products = productService.findAll(organizationId, pageable);
        final var pageCount = productService.getPageCount(organizationId, pageable.limit());
        final var response = new ProductsResponse(products.stream()
                .map(ProductDTO::fromModel)
                .toList(),
                pageCount);
        return Response.ok(response).build();
    }
}
