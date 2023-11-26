package com.distrupify.resources;

import com.distrupify.auth.services.TokenService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/v1/inventory/deposit")
@RequestScoped
public class InventoryDepositResource {

    @Inject
    TokenService tokenService;

    @Inject
    JsonWebToken jwt;
}
