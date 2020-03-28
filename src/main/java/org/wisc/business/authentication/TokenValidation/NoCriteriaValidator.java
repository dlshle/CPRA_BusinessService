package org.wisc.business.authentication.TokenValidation;

public class NoCriteriaValidator implements TokenValidationCriteria{
    @Override
    public boolean isValidToken(String token) {
        return token != null;
    }
}
