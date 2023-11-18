package com.distrupify.auth.services;

import com.distrupify.auth.dtos.TokenDTO;
import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.requests.LoginRequest;
import com.distrupify.auth.requests.SignupRequest;
import com.speedment.jpastreamer.application.JPAStreamer;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

@ApplicationScoped
public class AuthService {
    @Inject
    TokenService tokenService;

    @Inject
    PasswordService passwordService;

    @Inject
    JPAStreamer jpaStreamer;

    @Transactional
    public TokenDTO signup(SignupRequest signupInput) {
        final var user = UserEntity.builder()
                .email(signupInput.getEmail())
                .name(signupInput.getName())
                .password(passwordService.hash(signupInput.getPassword()))
                .organizationId(signupInput.getOrganizationId())
                .build();
        user.persist();

        return login(LoginRequest.builder()
                .email(signupInput.getEmail())
                .password(signupInput.getPassword())
                .build());
    }

    public TokenDTO login(LoginRequest loginInput) {
        final var user = jpaStreamer.stream(UserEntity.class)
                .filter(u -> u.getEmail().equals(loginInput.getEmail()))
                .findFirst().orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordService.isEqual(loginInput.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return tokenService.generateToken(user.getOrganizationId(), user.getEmail());
    }
}
