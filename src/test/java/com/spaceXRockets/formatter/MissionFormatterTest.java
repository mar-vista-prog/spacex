package com.spaceXRockets.formatter;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.enums.MissionStatus;
import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.enums.RocketStatus;
import com.spaceXRockets.service.formatter.MissionFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MissionFormatterTest {

    @Test
    void format_includesMissionDetailsAndFormattedRockets() {
        Rocket rocket = Rocket.builder()
                .name("Dragon 1")
                .status(RocketStatus.OnGround)
                .build();

        Mission mission = Mission.builder()
                .name("Mars")
                .status(MissionStatus.InProgress)
                .build();

        mission.addRocket(rocket);
        rocket.setMission(mission);

        String result = MissionFormatter.format(mission);

        assertTrue(result.contains("• Mars - InProgress - Dragons: 1"));
        assertTrue(result.contains("o Dragon 1 - On ground"));
    }
}