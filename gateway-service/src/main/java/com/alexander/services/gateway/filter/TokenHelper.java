package com.alexander.services.gateway.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Component
@Slf4j
public class TokenHelper {

    private final PublicKey key;

    public TokenHelper(@Value("${jwt.public.key}") String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key)));
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return !isRefreshToken(token) && !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        } catch (SignatureException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean isRefreshToken(final String token) {
        String tokenType = extractClaim(token, claims -> claims.get("tokenType", String.class));
        return Objects.equals(tokenType, "refresh");
    }

    public boolean isTokenExpired(final String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn(e.getMessage(), e);
            return true;
        }
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(final String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

}
