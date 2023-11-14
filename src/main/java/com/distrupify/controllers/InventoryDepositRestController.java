package com.distrupify.controllers;

import com.distrupify.requests.InventoryDepositCreateRequest;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/inventory/deposit")
public class InventoryDepositRestController {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response deposit(@Valid InventoryDepositCreateRequest createRequest) {
        return Response.ok().build();
    }
}
