package com.spaceXRockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MissionTest {

    private Mission mission;
    private Rocket rocket1;
    private Rocket rocket2;

    @BeforeEach
    void setUp() {
        mission = Mission.builder()
                .name("TestMission")
                .status(MissionStatus.Scheduled)
                .build();

        rocket1 = Rocket.builder().name("Dragon 1").build();
        rocket2 = Rocket.builder().name("Dragon 2").build();
    }

    @Test
    void addRocket_shouldAddRocketToSet() {
        mission.addRocket(rocket1);
        assertEquals(1, mission.getRockets().size());
        assertTrue(mission.getRockets().contains(rocket1));
    }

    @Test
    void addRocket_shouldIgnoreNulls() {
        mission.addRocket(null);
        assertTrue(mission.getRockets().isEmpty());
    }

    @Test
    void addRocket_shouldNotDuplicateSameInstance() {
        mission.addRocket(rocket1);
        mission.addRocket(rocket1);
        assertEquals(1, mission.getRockets().size());
    }

    @Test
    void clearRockets_shouldEmptyTheSet() {
        mission.addRocket(rocket1);
        mission.addRocket(rocket2);
        mission.clearRockets();

        assertTrue(mission.getRockets().isEmpty());
    }

    @Test
    void getRockets_shouldReturnUnmodifiableSet() {
        mission.addRocket(rocket1);
        Set<Rocket> rockets = mission.getRockets();
        assertThrows(UnsupportedOperationException.class, () -> rockets.add(rocket2));
    }

    @Test
    void missionShouldHaveDefaultStatus() {
        Mission m = Mission.builder().name("DefaultMission").build();
        assertEquals(MissionStatus.Scheduled, m.getStatus());
    }

    @Test
    void missionShouldAllowStatusUpdate() {
        mission.setStatus(MissionStatus.InProgress);
        assertEquals(MissionStatus.InProgress, mission.getStatus());
    }

    @Test
    void missionShouldRequireNonNullName() {
        assertThrows(NullPointerException.class, () -> Mission.builder().name(null).build());
    }
}