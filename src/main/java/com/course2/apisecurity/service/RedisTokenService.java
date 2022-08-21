package com.course2.apisecurity.service;

import com.course2.apisecurity.util.HmacUtil;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.course2.apisecurity.entity.RedisToken;
import com.course2.apisecurity.util.SecureStringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.lettuce.core.api.StatefulRedisConnection;
//@Service
public class RedisTokenService {

    @Autowired
    private StatefulRedisConnection<String, String> redisConnection;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String HMAC_SECRET = "theHmacSecretKey";

    public String store(RedisToken token) {
        var tokenId = SecureStringUtil.randomString(30);
        String tokenJson;
        try {
            tokenJson = objectMapper.writeValueAsString(token);
            redisConnection.sync().set(tokenId, tokenJson);
            redisConnection.sync().expire(tokenId, 15 * 60);

            var hmac = HmacUtil.hmac(tokenId, HMAC_SECRET);
            return StringUtils.join(tokenId, ".",hmac);
        }
        catch(Exception e){
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public Optional<RedisToken> read(String bearerToken) {

        try{
             var tokens = StringUtils.split(bearerToken,".");
             if (!HmacUtil.isHmacMatch(tokens[0], HMAC_SECRET , tokens[1])){
                 return Optional.empty();
             }
             var tokenJson = redisConnection.sync().get(tokens[0]);
             if (StringUtils.isBlank(tokenJson)){
                 return Optional.empty();
             }
             var token = objectMapper.readValue(tokenJson, RedisToken.class);
             return Optional.of(token);
        }
        catch(Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete(String tokenId) {
           redisConnection.sync().del(tokenId);
    }

}
