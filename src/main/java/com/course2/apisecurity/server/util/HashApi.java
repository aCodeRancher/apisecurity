package com.course2.apisecurity.server.util;

import com.course2.apisecurity.api.request.util.OriginalStringRequest;
import com.course2.apisecurity.util.HashUtil;
import com.course2.apisecurity.util.SecureStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hash")
public class HashApi {

    private static final int SALT_LENGTH = 16;
    private final Map<String, String> mapSalt = new HashMap<String, String>();

    @GetMapping(value = "/sha256", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String sha256(@RequestBody(required = true) OriginalStringRequest original) throws Exception {
        var salt = SecureStringUtil.randomString(SALT_LENGTH);
        mapSalt.put(original.getText(), salt);

        return HashUtil.sha256(original.getText(), salt);
    }

    @GetMapping(value = "/sha256/match", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String sha256Match(@RequestHeader(name = "X-Hash", required = true) String hashValue,
                              @RequestBody(required = true) OriginalStringRequest original) throws Exception {
        var salt = Optional.ofNullable(mapSalt.get(original.getText())).orElse(StringUtils.EMPTY);

        return HashUtil.isSha256Match(original.getText(), salt, hashValue) ? "match" : "NOT match";
    }

    @GetMapping(value = "/bcrypt", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bcrypt(@RequestBody(required = true) OriginalStringRequest original) throws Exception {
        var salt = SecureStringUtil.randomString(SALT_LENGTH);

        return HashUtil.bcrypt(original.getText(), salt);
    }

    @GetMapping(value = "/bcrypt/match", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String bcryptMatch(@RequestHeader(name = "X-Hash", required = true) String hashValue,
                              @RequestBody(required = true) OriginalStringRequest original) throws Exception {
        return HashUtil.isBcryptMatch(original.getText(), hashValue) ? "match" : "NOT match";
    }
}
