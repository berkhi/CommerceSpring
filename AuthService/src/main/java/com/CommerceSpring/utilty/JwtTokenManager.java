package com.CommerceSpring.utilty;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.CommerceSpring.exception.AuthServiceException;
import com.CommerceSpring.exception.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtTokenManager {
    @Value("${auth.secret.secret-key}")
    String secretKey;
    @Value("${auth.secret.issuer}")
    String issuer;
    private final Long EXDATE = 1000L * 60 * 60 ;

    public Optional<String> createToken(UUID authId) {
        String token;
        try {
            token = JWT.create()
                    .withClaim("authId", authId.toString()) // UUID'yi String olarak sakla
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXDATE))
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public Optional<UUID> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT == null)
                return Optional.empty();

            String authId = decodedJWT.getClaim("authId").asString(); // String olarak alınır
            return Optional.of(UUID.fromString(authId)); // String'den UUID'ye dönüştürülür
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UUID> getIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            if (decodedJWT == null) {
                throw new AuthServiceException(ErrorType.INVALID_TOKEN);
            }

            String id = decodedJWT.getClaim("authId").asString(); // String olarak alınır
            return Optional.of(UUID.fromString(id)); // String'den UUID'ye dönüştürülür
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
    }


}