package com.course2.apisecurity.server.cors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cors/v1")
public class MadMaxApi {

    @GetMapping(value = "/mad", produces = MediaType.TEXT_PLAIN_VALUE)
 //	@CrossOrigin(origins = "http://127.0.0.1:8080")
    public String mad() {
        return "Mad";
    }

    @PostMapping(value = "/max", produces = MediaType.TEXT_PLAIN_VALUE)
 //	@CrossOrigin(origins = { "http://127.0.0.1:8080", "http://172.30.240.1:8080" })
    public String max() {
        return "Max";
    }

}
