package com.distrupify.validations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExistingProductIdValidator
        implements ConstraintValidator<ExistingProductId, Long> {
    private static final Logger LOGGER = Logger.getLogger(ExistingProductIdValidator.class);

//    @Inject
//    ProductService productService;
//
//    @Inject
//    JsonWebToken jwt;

    @Override
    public boolean isValid(Long productId,
                           ConstraintValidatorContext ctx) {
//        final var organizationIdClaim = jwt.getClaim("organization_id");
//        if (organizationIdClaim == null) {
//            LOGGER.info("productId: " + productId + " for organization: null is invalid");
//            return false;
//        }
//
//        final var organizationId = Long.valueOf(String.valueOf(organizationIdClaim));
//        final var product = productService.findProductById(organizationId, productId);
//
//        if (product.isPresent()) {
//            LOGGER.info("productId: " + productId + " for organization: " + organizationId + " is valid");
//            return true;
//        }
//
//        LOGGER.info("productId: " + productId + " for organization: " + organizationId + " is invalid");
        return false;
    }
}