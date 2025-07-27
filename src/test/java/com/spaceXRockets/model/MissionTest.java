package com.spaceXRockets.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MissionTest {

    @Test
    void builder_defaultsAreSetCorrectly() {
        Mission mission = Mission.builder().name("m").build();

        assertEquals("m", mission.getName());
        assertEquals(MissionStatus.Scheduled, mission.getStatus());
        assertNotNull(mission.getRockets());
        assertTrue(mission.getRockets().isEmpty());
    }

    @Test
    void addRocket_addsRocketToMission() {
        Mission mission = Mission.builder().name("m").build();
        Rocket rocket = Rocket.builder().name("Falcon").build();

        mission.addRocket(rocket);

        assertEquals(1, mission.getRockets().size());
        assertTrue(mission.getRockets().contains(rocket));
    }

    @Test
    void addRocket_doesNotAddNullRocket() {
        Mission mission = Mission.builder().name("m").build();

        mission.addRocket(null);

        assertTrue(mission.getRockets().isEmpty());
    }

    @Test
    void clearRockets_removesAllRockets() {
        Rocket rocket = Rocket.builder().name("Dragon").build();
        Set<Rocket> rockets = new HashSet<>();
        rockets.add(rocket);
        Mission mission = Mission.builder().name("m").rockets(rockets).build();

        mission.clearRockets();

        assertTrue(mission.getRockets().isEmpty());
    }

    @Test
    void getRockets_returnsUnmodifiableSet() {
        Rocket rocket = Rocket.builder().name("Red Dragon").build();
        Mission mission = Mission.builder().name("m").rockets(Set.of(rocket)).build();

        Set<Rocket> rockets = mission.getRockets();

        assertThrows(UnsupportedOperationException.class, () -> rockets.add(Rocket.builder().name("New").build()));
    }

    @Test
    void toString_formatsMissionAndRockets() {
        Rocket rocket1 = Rocket.builder().name("Alpha").status(RocketStatus.InSpace).build();
        Rocket rocket2 = Rocket.builder().name("Beta").status(RocketStatus.OnGround).build();
        Mission mission = Mission.builder()
                .name("Mars Mission")
                .status(MissionStatus.InProgress)
                .rockets(Set.of(rocket1, rocket2))
                .build();

        String result = mission.toString();

        assertTrue(result.startsWith("• Mars Mission - InProgress - Dragons: 2"));
        assertTrue(result.contains("Alpha"));
        assertTrue(result.contains("Beta"));
    }
}