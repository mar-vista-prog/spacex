package com.spaceXRockets.formatter;

import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.model.RocketStatus;
import com.spaceXRockets.service.formatter.RocketFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RocketFormatterTest {

    @Test
    void format_returnsFormattedString() {
        Rocket rocket = Rocket.builder()
                .name("DragonX")
                .status(RocketStatus.InSpace)
                .build();

        String result = RocketFormatter.format(rocket);

        assertEquals("o DragonX - In space\r\n", result);
    }
}