package org.wisc.business.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.wisc.business.model.UserModel.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public class SecurityUtil {
    public static final long EXPIRATION_PERIOD = 1800000l;

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(hashString(password, salt));
            byte[] src = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte aSrc : src) {
                String s = Integer.toHexString(aSrc & 0xFF);
                if (s.length() < 2) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    private static byte[] hashString(String x, String y) {
        if (x.length() < y.length())
            return hashString(y, x);
        byte[] result = new byte[x.length()];
        int i;
        for (i =0; i < y.length(); i++) {
            result[i] = hashChar(x.charAt(i), y.charAt(i));
        }
        for (;i<x.length();i++) {
            result[i] = hashChar(x.charAt(i), y.charAt(i%y.length()));
        }
        return result;
    }

    private static byte hashChar(char x, char y) {
        return (byte) (((x^y)>>2)|x);
    }

    public static String generateSalt(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    /**
     * Generates a token with user issued at current system time in mills
     * @param user user issued to
     * @return signed token
     */
    public static String generateToken(User user) {
        String token;
        token = JWT.create()
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withAudience(user.getId())
                        .sign(Algorithm.HMAC256(user.getPassword())); //no
        // worries, password is hashed
        return token;
    }

    public static String getUserIdFromToken(String token) {
        Object o = JWT.decode(token);
        List<String> audience = JWT.decode(token).getAudience();
        return JWT.decode(token).getAudience().get(0);
    }

    public static boolean verifyTokenWithPassword(String token,
                                                 String password) {
        JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(password)).build();
        try {
            verifier.verify(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
}
