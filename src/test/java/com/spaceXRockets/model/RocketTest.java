package com.spaceXRockets.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RocketTest {

    @Test
    void builder_defaultsAreSetCorrectly() {
        Rocket rocket = Rocket.builder().name("r").build();

        assertEquals("r", rocket.getName());
        assertEquals(RocketStatus.OnGround, rocket.getStatus());
        assertNull(rocket.getMission());
    }

    @Test
    void builder_setsAllFields() {
        Mission mission = Mission.builder().name("Luna").build();
        Rocket rocket = Rocket.builder()
                .name("DragonX")
                .status(RocketStatus.InSpace)
                .mission(mission)
                .build();

        assertEquals("DragonX", rocket.getName());
        assertEquals(RocketStatus.InSpace, rocket.getStatus());
        assertEquals(mission, rocket.getMission());
    }

    @Test
    void toString_returnsFormattedString() {
        Rocket rocket = Rocket.builder()
                .name("Falcon 9")
                .status(RocketStatus.InSpace)
                .build();

        String expected = "o Falcon 9 - In space\n";
        assertEquals(expected, rocket.toString());
    }

    @Test
    void reversedStatusNameComparator_sortsByStatusDescendingThenName() {
        Rocket r1 = Rocket.builder().name("Alpha").status(RocketStatus.OnGround).build();
        Rocket r2 = Rocket.builder().name("Beta").status(RocketStatus.InSpace).build();
        Rocket r3 = Rocket.builder().name("Gamma").status(RocketStatus.OnGround).build();
        Rocket r4 = Rocket.builder().name("Zeta").status(RocketStatus.InSpace).build();

        List<Rocket> rockets = Arrays.asList(r1, r2, r3, r4);
        rockets.sort(Rocket.REVERSED_STATUS_NAME_COMPARATOR);

        // InSpace > OnGround, names sorted ascending within same status
        assertEquals(List.of(r2, r4, r1, r3), rockets);
    }

    @Test
    void missionCanBeAssignedAndRetrieved() {
        Rocket rocket = Rocket.builder().name("Explorer").build();
        Mission mission = Mission.builder().name("Mars Mission").build();

        rocket.setMission(mission);

        assertEquals(mission, rocket.getMission());
    }

    @Test
    void statusCanBeUpdated() {
        Rocket rocket = Rocket.builder().name("r").status(RocketStatus.OnGround).build();
        assertEquals(RocketStatus.OnGround, rocket.getStatus());

        rocket.setStatus(RocketStatus.InSpace);
        assertEquals(RocketStatus.InSpace, rocket.getStatus());
    }
}