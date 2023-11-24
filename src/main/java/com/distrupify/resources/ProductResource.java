package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import com.distrupify.dto.ProductDTO;
import com.distrupify.services.ProductService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public Response getProducts() {
        final var organizationId = tokenService.getOrganizationId(jwt);
        final var products = productService.findAll(organizationId);
        return Response.ok(Map.of("products", products)).build();
    }
}
