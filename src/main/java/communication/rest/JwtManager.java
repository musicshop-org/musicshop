package communication.rest;

import communication.rest.api.RestLoginService;
import io.jsonwebtoken.*;
import sharedrmi.domain.valueobjects.Role;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.LinkedList;
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

    @SuppressWarnings("unused")
    public static String getId(String jwt) {
        return getClaims(jwt).getId();
    }

    @SuppressWarnings("unused")
    public static String getIssuer(String jwt) {
        return getClaims(jwt).getIssuer();
    }

    @SuppressWarnings("unused")
    public static Date getIssuedAt(String jwt) {
        return getClaims(jwt).getIssuedAt();
    }

    public static String getEmailAddress(String jwt) {
        return getClaims(jwt).get("email", String.class);
    }

    @SuppressWarnings("unused")
    public static Date getExpiration(String jwt) {
        return getClaims(jwt).getExpiration();
    }

    private static Claims getClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static List<Role> getRoles(String jwt) {
        @SuppressWarnings("unchecked")
        List<String> rolesString = getClaims(jwt).get("roles", List.class);
        List<Role> roles = new LinkedList<>();

        for (String role : rolesString) {
            roles.add(Role.valueOf(role));
        }

        return roles;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidToken(String jwt) {
        try {
            getClaims(jwt);
        } catch (JwtException e) {
            return false;
        }

        return true;
    }
}