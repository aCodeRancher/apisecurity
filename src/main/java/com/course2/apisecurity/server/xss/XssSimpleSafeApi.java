package com.course2.apisecurity.server.xss;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.owasp.encoder.Encode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/xss/safe/v1")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class XssSimpleSafeApi {
    private Tika tika = new Tika();

    @GetMapping(value = "/greeting", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greeting(@RequestParam(value = "name", required = true) String name) {
        var nowHour = LocalTime.now().getHour();

        var greetString = (nowHour >= 6 && nowHour < 18) ? ("Good morning " + name) : ("Good night " + name);

        return Encode.forHtml(greetString);
    }

    @GetMapping(value = "/file")
    public ResponseEntity<Resource> downloadFile() throws IOException {
        var resource = new ClassPathResource("static/fileWithXss.csv");

        var determinedContentType = tika.detect(resource.getInputStream());

        if (StringUtils.isBlank(determinedContentType)
                || StringUtils.equalsIgnoreCase(determinedContentType, MediaType.TEXT_HTML_VALUE)) {
            determinedContentType = MediaType.TEXT_PLAIN_VALUE;
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(determinedContentType)).body(resource);
    }

}
