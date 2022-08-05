package com.course2.apisecurity.server.dos;

import com.course2.apisecurity.api.response.dos.HexColorPaginationResponse;
import com.course2.apisecurity.entity.HexColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    @GetMapping(value = "/random-colors-pagination", produces = MediaType.APPLICATION_JSON_VALUE)
    public HexColorPaginationResponse randomColors(@RequestParam(required = true, name = "page") int page,
                                                   @Valid @Min(10) @Max(100) @RequestParam(required = true, name = "size") int size) {
        var startIndex = (page - 1) * size;
        var sublist = hexColors.subList(startIndex, startIndex + size);

        var response = new HexColorPaginationResponse();
        response.setColors(sublist);
        response.setCurrentPage(page);
        response.setSize(size);
        response.setTotalPages((int) Math.ceil(COLORS_SIZE / size));

        return response;
    }

}
