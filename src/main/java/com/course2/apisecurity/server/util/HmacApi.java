package com.course2.apisecurity.server.util;

import com.course2.apisecurity.api.request.util.HmacRequest;
import com.course2.apisecurity.util.HmacUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hmac")
public class HmacApi {
    public static final String SECRET_KEY = "The123HmacSecretKey";

    private static final String MESSAGE_DELIMITER = "\n";

    @GetMapping(value = "/calculate", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String hmac(@RequestHeader(required = true, name = "X-Verb-Calculate") String verbCalculate,
                       @RequestHeader(required = true, name = "X-Uri-Calculate") String uriCalculate,
                       @RequestHeader(required = true, name = "X-Register-Date") String registerDate,
                       @RequestHeader(required = true, name = "X-Nonce") String nonce,
                       @RequestBody(required = true) HmacRequest requestBody) throws Exception {
        var hmacMessage = constructHmacMessage(verbCalculate, uriCalculate, requestBody.getAmount(),
                requestBody.getFullName(), registerDate, nonce);

        return HmacUtil.hmac(hmacMessage, SECRET_KEY);
    }

    public static String constructHmacMessage(String verbCalculate, String uriCalculate, int amount, String fullName,
                                              String registerDate, String nonce) {
        var sb = new StringBuilder();

        sb.append(verbCalculate.toLowerCase());
        sb.append(MESSAGE_DELIMITER);
        sb.append(uriCalculate);
        sb.append(MESSAGE_DELIMITER);
        sb.append(amount);
        sb.append(MESSAGE_DELIMITER);
        sb.append(fullName);
        sb.append(MESSAGE_DELIMITER);
        sb.append(registerDate);
        sb.append(MESSAGE_DELIMITER);
        sb.append(nonce);

        return sb.toString();
    }

    @RequestMapping(value = "/info", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String info(@RequestBody(required = true) HmacRequest original) {
        return "The request body is : " + original;
    }
}
