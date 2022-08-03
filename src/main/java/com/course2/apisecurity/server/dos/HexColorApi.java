package com.course2.apisecurity.server.dos;

import com.course2.apisecurity.entity.HexColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/dos/v1")
public class HexColorApi {

    private static final int COLORS_SIZE = 1_000_000;

    private List<HexColor> hexColors;

    private String randomColorHex() {
        var randomInt = ThreadLocalRandom.current().nextInt(0xffffff + 1);

        // format as hash and leading zero (hex color code)
        return String.format("#%06x", randomInt);
    }

    public HexColorApi() {
        hexColors = IntStream.rangeClosed(1, COLORS_SIZE).boxed().parallel().map(v -> {
            var hexColor = new HexColor();
            hexColor.setId(v);
            hexColor.setHexColor(randomColorHex());

            return hexColor;
        }).collect(Collectors.toList());
    }

    @GetMapping(value = "/random-colors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HexColor> randomColors() {
        return hexColors;
    }

}
