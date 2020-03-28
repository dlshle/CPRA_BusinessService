package org.wisc.business.authentication.TokenValidation;

import com.auth0.jwt.JWT;
import org.wisc.business.authentication.SecurityUtil;

import java.util.Date;

public class TimePeriodTokenValidator implements  TokenValidationCriteria {
    @Override
    public boolean isValidToken(String token) {
        if (token == null)
            return false;
        Date issueDate = JWT.decode(token).getIssuedAt();
        return System.currentTimeMillis() - issueDate.getTime() > SecurityUtil.EXPIRATION_PERIOD;
    }
}
