package communication.rest;

import communication.rest.api.RestLoginService;
import io.jsonwebtoken.*;
import sharedrmi.domain.valueobjects.Role;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtManager {

    private static final byte[] SECRET_KEY = "SECRET_OF_MUSIC-SHOP".getBytes();
    private static final String ISSUER = "Musicshop";
    private static final RestLoginService restLoginService = new RestLoginServiceImpl();


    public static String createJWT(String emailAddress, long ttlMillis) {

        // JWT signature algorithm to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Key signingKey = new SecretKeySpec(SECRET_KEY, signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        List<Role> roles = restLoginService.getRole(emailAddress);

        // set Jwt Claims
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .claim("email", emailAddress)
                .claim("roles", roles)
                .signWith(signatureAlgorithm, signingKey);

        // set Jwt-Expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // build the Jwt and serialize it to a compact, URL-safe string
        return builder.compact();
    }

    public static String getId(String jwt) {
        return getClaims(jwt).getId();
    }

    public static String getIssuer(String jwt) {
        return getClaims(jwt).getIssuer();
    }

    public static Date getIssuedAt(String jwt) {
        return getClaims(jwt).getIssuedAt();
    }

    public static String getEmailAddress(String jwt) {
        return getClaims(jwt).get("email", String.class);
    }

    public static List<Role> getRoles(String jwt) {
        return getClaims(jwt).get("roles", List.class);
    }

    public static Date getExpiration(String jwt) {
        return getClaims(jwt).getExpiration();
    }

    public static Claims decodeJwt(String jwt) {
        return getClaims(jwt);
    }

    public static boolean validateJwt(String jwt) {
        try {
            decodeJwt(jwt);
        }
        catch (JwtException e) {
            return false;
        }

        return true;
    }

    private static Claims getClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();
    }
}
