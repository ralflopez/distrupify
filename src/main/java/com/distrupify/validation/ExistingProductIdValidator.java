package com.distrupify.validation;

import com.distrupify.product.ProductService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExistingProductIdValidator
        implements ConstraintValidator<ExistingProductId, Long> {
    private static final Logger LOGGER = Logger.getLogger(ExistingProductIdValidator.class);

    @Inject
    ProductService productService;

    // TODO: investigate if can be validated by organization_id from token
    @Override
    public boolean isValid(Long productId,
                           ConstraintValidatorContext ctx) {
        final var product = productService.findProductById(productId);

        if (product.isPresent()) {
            LOGGER.info("productId: " + productId + " is valid");
            return true;
        }

        LOGGER.info("productId: " + productId + " is invalid");
        return false;
    }
}