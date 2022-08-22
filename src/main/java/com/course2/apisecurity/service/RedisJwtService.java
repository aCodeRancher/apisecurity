package com.course2.apisecurity.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.course2.apisecurity.entity.JwtData;
import com.course2.apisecurity.util.SecureStringUtil;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

@Service
public class RedisJwtService {
    private static final String HMAC_SECRET = "theHmacSecretKeyForJwt";

    private static final String ISSUER = "apisecurity.com";

    private static final String[] AUDIENCE = { "https://apisecurity.com", "https://www.apisecurity.com" };

    private static final String SAMPLE_PRIVATE_CLAIM = "just-some-private-claim-to-be-validated";

    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;

    public String store(JwtData jwtData) {
        var algorithm = Algorithm.HMAC256(HMAC_SECRET);
        var expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        var expiresAtDate = Date.from(expiresAt.toInstant(ZoneOffset.UTC));
        var jwtId = SecureStringUtil.randomString(30);
        redisConnection.sync().set(jwtId, StringUtils.EMPTY);
        redisConnection.sync().expireat(jwtId, expiresAtDate);

        return JWT.create().withSubject(jwtData.getUsername()).withIssuer(ISSUER).withAudience(AUDIENCE)
                .withExpiresAt(expiresAtDate)
                .withClaim("dummyAttribute", jwtData.getDummyAttribute())
                .withClaim("samplePrivateClaim", SAMPLE_PRIVATE_CLAIM).withJWTId(jwtId).sign(algorithm);
    }

    public Optional<JwtData> read(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(HMAC_SECRET);
            var jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).withAnyOfAudience(AUDIENCE).acceptExpiresAt(60)
                    .withClaim("samplePrivateClaim", SAMPLE_PRIVATE_CLAIM).build();
            var decodedJwt = jwtVerifier.verify(jwtToken);

            var jwtId = redisConnection.sync().get(decodedJwt.getId());
            if (jwtId==null){
                return Optional.empty();
            }
            var jwtData = new JwtData();
            jwtData.setUsername(decodedJwt.getSubject());
            jwtData.setDummyAttribute(decodedJwt.getClaim("dummyAttribute").asString());

            return Optional.of(jwtData);
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }

    public void delete(String jwtToken){
        var algorithm = Algorithm.HMAC256(HMAC_SECRET);
        var jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).withAnyOfAudience(AUDIENCE)
                .acceptExpiresAt(60).withClaim("samplePrivateClaim",SAMPLE_PRIVATE_CLAIM).build();
        var decodedJwt = jwtVerifier.verify(jwtToken);
        redisConnection.sync().del(decodedJwt.getId());
     }
}
