package com.alexander.services.identity.authencation.jwt;

import com.alexander.services.identity.entity.User;
import com.alexander.services.library.util.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Component
@Slf4j
public class TokenProvider {

    private final Integer expirationMinuteInMs;
    private final Long refreshExpirationDateInMs;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public TokenProvider(@Value("${jwt.private.key}") String privateKey, @Value("${jwt.public.key}") String publicKey, @Value("${jwt.access.expiration.minute.in.ms}") Integer expirationMinuteInMs,
                         @Value("${jwt.refresh.expiration.date.in.ms}") Long refreshExpirationDateInMs) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        this.expirationMinuteInMs = expirationMinuteInMs;
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return generateToken(expirationMinuteInMs, user, TokenType.ACCESS.getType());
    }

    public String generateRefreshToken(User user) {
        return generateToken(refreshExpirationDateInMs, user, TokenType.REFRESH.getType());
    }

    public boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isRefreshToken(final String token) {
        String tokenType = extractClaim(token, claims -> claims.get(Constants.TOKEN_TYPE, String.class));
        return Objects.equals(tokenType, TokenType.REFRESH.getType());
    }

    private String generateToken(final long expiration, final User user, final String tokenType) {
        long current = System.currentTimeMillis();
        return Jwts.builder().header().type("JWT").and().subject(user.getEmail())
                .claim(Constants.USER_ID, user.getId()).claim(Constants.USER_ROLE, user.getRole().getName()).claim(Constants.TOKEN_TYPE, tokenType)
                .issuedAt(new Date(current)).expiration(new Date(current + expiration))
                .signWith(privateKey, Jwts.SIG.RS256).compact();
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(final String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            log.warn(e.getMessage(), e);
            return true;
        }
    }

}
