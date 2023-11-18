package com.distrupify.auth.services;

import com.distrupify.auth.entities.UserEntity;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Optional;

@ApplicationScoped
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    @Inject
    JPAStreamer jpaStreamer;

    public Optional<UserEntity> findByEmail(Long organizationId, String email) {
        return jpaStreamer.stream(UserEntity.Joins.USER_IJ_ORGANIZATION)
                .filter(u -> u.getOrganizationId().equals(organizationId))
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }
}
