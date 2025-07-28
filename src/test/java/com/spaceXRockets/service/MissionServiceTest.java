package com.spaceXRockets.service;

import com.spaceXRockets.model.*;
import com.spaceXRockets.repository.inmemory.InMemoryMissionRepository;
import com.spaceXRockets.repository.inmemory.InMemoryRocketRepository;
import com.spaceXRockets.service.impl.MissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MissionServiceTest {

    private MissionService missionService;

    @BeforeEach
    void setup() {
        MissionStatusUpdater stubUpdater = mission -> {
            if (mission.getRockets().isEmpty()) {
                mission.setStatus(MissionStatus.Scheduled);
            } else if (mission.getRockets().stream().anyMatch(r -> r.getStatus() == RocketStatus.InRepair)) {
                mission.setStatus(MissionStatus.Pending);
            } else {
                mission.setStatus(MissionStatus.InProgress);
            }
        };

        missionService = new MissionServiceImpl(
                new InMemoryMissionRepository(),
                new InMemoryRocketRepository(),
                stubUpdater
        );
    }

    @Test
    void testAddMissionAndRetrieveSummary() {
        Mission mission = createMission("Mars");
        addMission(mission);

        List<Mission> summary = missionService.getMissionsSummary();

        assertEquals(1, summary.size());
        assertEquals("Mars", summary.get(0).getName());
        assertEquals(0, summary.get(0).getRockets().size());
    }

    @Test
    void testAddRocketToMission() {
        Mission mission = createMission("Moon");
        Rocket rocket = createRocket("Falcon");

        addMission(mission);
        addRocket(rocket);
        assignRocketToMission(rocket, mission);

        assertEquals(1, mission.getRockets().size());
        assertEquals(mission, rocket.getMission());
        assertEquals(MissionStatus.InProgress, mission.getStatus());
    }

    @Test
    void testChangeMissionStatusToEnded() {
        Mission mission = createMission("Venus");
        Rocket rocket = createRocket("Dragon");

        mission.addRocket(rocket);
        rocket.setMission(mission);
        addMission(mission);

        missionService.changeMissionStatus(mission, MissionStatus.Ended);

        assertEquals(MissionStatus.Ended, mission.getStatus());
        assertTrue(mission.getRockets().isEmpty());
        assertNull(rocket.getMission());
    }

    @Test
    void testAssignRocketsToMission() {
        Mission mission = createMission("Luna");
        Rocket r1 = createRocket("R1");
        Rocket r2 = createRocket("R2");

        addMission(mission);
        addRocket(r1);
        addRocket(r2);

        missionService.assignRocketsToMission(Set.of(r1, r2), mission);

        assertEquals(2, mission.getRockets().size());
        assertTrue(mission.getRockets().contains(r1));
        assertTrue(mission.getRockets().contains(r2));
    }

    @Test
    void testChangeRocketStatusToInRepair() {
        Mission mission = createMission("Europa");
        Rocket rocket = createRocket("Damaged");

        mission.addRocket(rocket);
        rocket.setMission(mission);
        addMission(mission);

        missionService.changeRocketStatus(rocket, RocketStatus.InRepair);

        assertEquals(RocketStatus.InRepair, rocket.getStatus());
        assertEquals(MissionStatus.Pending, mission.getStatus());
    }

    @Test
    void testMissionSummaryOrdering() {
        Mission m1 = Mission.builder().name("Mars").status(MissionStatus.Scheduled).build();
        Set<Rocket> lunaRockets = new HashSet<>();
        lunaRockets.add(Rocket.builder().name("Dragon 1").status(RocketStatus.OnGround).build());
        lunaRockets.add(Rocket.builder().name("Dragon 2").status(RocketStatus.OnGround).build());
        Mission m2 = Mission.builder().name("Luna1").status(MissionStatus.Pending).rockets(lunaRockets).build();
        Mission m3 = Mission.builder().name("Double Landing").status(MissionStatus.Ended).build();
        Set<Rocket> transitRockets = new HashSet<>();
        transitRockets.add(Rocket.builder().name("Red Dragon").status(RocketStatus.OnGround).build());
        transitRockets.add(Rocket.builder().name("Dragon XL").status(RocketStatus.InSpace).build());
        transitRockets.add(Rocket.builder().name("Falcon Heavy").status(RocketStatus.InSpace).build());

        Mission m4 = Mission.builder().name("Transit").status(MissionStatus.InProgress).rockets(transitRockets).build();
        Mission m5 = Mission.builder().name("Luna2").status(MissionStatus.Scheduled).build();
        Mission m6 = Mission.builder().name("Vertical Landing").status(MissionStatus.Ended).build();

        missionService.addMission(m1);
        missionService.addMission(m2);
        missionService.addMission(m3);
        missionService.addMission(m4);
        missionService.addMission(m5);
        missionService.addMission(m6);

        String result = missionService.getMissionsSummaryByNumberOfRockets();

        assertEquals("• Transit - InProgress - Dragons: 3\r\n" +
                "o Dragon XL - In space\r\n" +
                "o Falcon Heavy - In space\r\n" +
                "o Red Dragon - On ground\r\n" +
                "• Luna1 - Pending - Dragons: 2\r\n" +
                "o Dragon 1 - On ground\r\n" +
                "o Dragon 2 - On ground\r\n" +
                "• Vertical Landing - Ended - Dragons: 0\r\n" +
                "• Mars - Scheduled - Dragons: 0\r\n" +
                "• Luna2 - Scheduled - Dragons: 0\r\n" +
                "• Double Landing - Ended - Dragons: 0\r\n", result);
    }

    @Test
    void testAssignRocketAlreadyAssignedToMission_shouldDoNothing() {
        Mission m1 = createMission("Alpha");
        Mission m2 = createMission("Beta");
        Rocket rocket = createRocket("Reusable");

        addMission(m1);
        addMission(m2);
        addRocket(rocket);
        assignRocketToMission(rocket, m1);

        missionService.assignRocketToMission(rocket, m2); // should not reassign

        assertEquals(1, m1.getRockets().size());
        assertEquals(0, m2.getRockets().size());
        assertEquals(m1, rocket.getMission());
    }

    @Test
    void testAssignRocketToEndedMission_shouldDoNothing() {
        Mission endedMission = createMission("Terminated");
        endedMission.setStatus(MissionStatus.Ended);
        Rocket rocket = createRocket("Archived");

        addMission(endedMission);
        addRocket(rocket);
        assignRocketToMission(rocket, endedMission);

        assertEquals(0, endedMission.getRockets().size());
        assertNull(rocket.getMission());
    }

    @Test
    void testAddNullRocketToMission_shouldNotThrow() {
        Mission mission = createMission("NullSafe");
        addMission(mission);

        // should not throw
        mission.addRocket(null);

        assertEquals(0, mission.getRockets().size());
    }

    @Test
    void testChangeRocketStatusWithoutMission_shouldNotThrow() {
        Rocket rocket = createRocket("Independent");
        addRocket(rocket);

        // should not throw or affect any mission
        missionService.changeRocketStatus(rocket, RocketStatus.InRepair);

        assertEquals(RocketStatus.InRepair, rocket.getStatus());
    }

    @Test
    void testChangeMissionStatusWithNoRockets_shouldSetStatus() {
        Mission mission = createMission("EmptyMission");
        addMission(mission);

        missionService.changeMissionStatus(mission, MissionStatus.Ended);

        assertEquals(MissionStatus.Ended, mission.getStatus());
        assertTrue(mission.getRockets().isEmpty());
    }


    private Mission createMission(String name) {
        return Mission.builder().name(name).build();
    }

    private Rocket createRocket(String name) {
        return Rocket.builder().name(name).build();
    }

    private void addMission(Mission mission) {
        missionService.addMission(mission);
    }

    private void addRocket(Rocket rocket) {
        missionService.addRocket(rocket);
    }

    private void assignRocketToMission(Rocket rocket, Mission mission) {
        missionService.assignRocketToMission(rocket, mission);
    }
}
