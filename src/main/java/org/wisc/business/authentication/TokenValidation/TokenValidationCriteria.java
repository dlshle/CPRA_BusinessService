package org.wisc.business.authentication.TokenValidation;

public interface TokenValidationCriteria {
    boolean isValidToken(String token);
}

