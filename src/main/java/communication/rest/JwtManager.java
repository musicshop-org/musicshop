package communication.rest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

public class JwtManager {

    private static final byte[] SECRET_KEY = "SECRET_OF_MUSIC-SHOP".getBytes();
    private static final String ISSUER = "Musicshop";

    public static String createJWT(String subject, long ttlMillis) {

        // JWT signature algorithm to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Key signingKey = new SecretKeySpec(SECRET_KEY, signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // set JWT Claims
        JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey);

        // set JWT-Expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        // build the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();

        return claims;
    }

    public static Date getIssuedAt(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();

        return claims.getIssuedAt();
    }

    public static String getId(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();

        return claims.getId();
    }

    public static String getIssuer(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();

        return claims.getIssuer();
    }

    public static String getUsername(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();

        return claims.getSubject();
    }

    public static Date getExpiration(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt).getBody();

        return claims.getExpiration();
    }
}
