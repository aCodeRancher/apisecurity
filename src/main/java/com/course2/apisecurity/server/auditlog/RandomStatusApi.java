package com.course2.apisecurity.server.auditlog;

import com.course2.apisecurity.api.request.util.OriginalStringRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/log/v1")
public class RandomStatusApi {
    final static Logger logger = LoggerFactory.getLogger(RandomStatusApi.class);


    @GetMapping(value = "/random-status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> randomStatus(@RequestBody(required = false) OriginalStringRequest request) {
        var randomStatus = switch (ThreadLocalRandom.current().nextInt() % 3) {
            case 0:
                yield HttpStatus.CREATED;
            case 1:
                yield HttpStatus.NO_CONTENT;
            case 2:
                yield HttpStatus.PAYMENT_REQUIRED;
            default:
                yield HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(randomStatus).body(LocalTime.now().toString());
    }


}
