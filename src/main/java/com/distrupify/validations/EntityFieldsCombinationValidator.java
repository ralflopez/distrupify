package com.distrupify.validations;

import com.distrupify.auth.entities.UserEntity;
import com.distrupify.auth.resources.AuthResource;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;

@ApplicationScoped
public class EntityFieldsCombinationValidator {

    private static final Logger LOGGER = Logger.getLogger(EntityFieldsCombinationValidator.class);

    @Inject
    JPAStreamer jpaStreamer;

    public void checkUniqueEmailAndOrganizationOrThrow(Long organizationId, String email) {
        final var count = jpaStreamer.stream(UserEntity.class)
                .filter(u -> u.getOrganizationId().equals(organizationId))
                .filter(u -> u.getEmail().equals(email))
                .count();
        if (count != 0) {
            throw new BadRequestException("Invalid email");
        }
    }
}
