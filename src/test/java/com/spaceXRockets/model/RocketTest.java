package com.spaceXRockets.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RocketTest {

    @Test
    void rocketIsCreatedWithNameAndDefaultStatus() {
        Rocket rocket = Rocket.builder()
                .name("Falcon 9")
                .build();

        assertEquals("Falcon 9", rocket.getName());
        assertEquals(RocketStatus.OnGround, rocket.getStatus());
        assertNull(rocket.getMission());
    }

    @Test
    void setStatus_changesRocketStatus() {
        Rocket rocket = Rocket.builder()
                .name("Starship")
                .build();

        rocket.setStatus(RocketStatus.InSpace);

        assertEquals(RocketStatus.InSpace, rocket.getStatus());
    }

    @Test
    void setMission_assignsMissionToRocket() {
        Rocket rocket = Rocket.builder()
                .name("Dragon")
                .build();

        Mission mission = Mission.builder()
                .name("Mars Mission")
                .build();

        rocket.setMission(mission);

        assertEquals(mission, rocket.getMission());
    }

    @Test
    void nameIsNonNull() {
        assertThrows(NullPointerException.class, () -> {
            Rocket.builder()
                    .name(null)
                    .build();
        });
    }
}