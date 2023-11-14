package com.distrupify.auth.services;

import com.distrupify.auth.dtos.TokenDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.HashSet;

@ApplicationScoped
public class TokenService {
    private static final Logger LOGGER = Logger.getLogger(TokenService.class);

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    private String issuer;

    // TODO: add expiry
    // TODO: check what happens if token expires
    // TODO: polish roles
    public TokenDTO generateToken(Long organizationId, String email) {
        final var token = Jwt.issuer(issuer) // mp.jwt.verify.issuer = valid
                .upn(email) // preferred claim to use for the Principal seen via the container security APIs.
                .groups(new HashSet<>())
                .claim("organization_id", organizationId)
                .sign();

        return TokenDTO.builder()
                .token(token)
                .build();
    }

    public Long getOrganizationId(JsonWebToken jwt) {
        final var organizationIdClaim = jwt.getClaim("organization_id");
        return Long.valueOf(String.valueOf(organizationIdClaim));
    }
}
