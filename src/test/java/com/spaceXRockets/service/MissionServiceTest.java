package com.spaceXRockets.service;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.model.MissionStatus;
import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.model.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceImplTest {

    @InjectMocks
    private MissionService missionService;

    private Mission mission;
    private Rocket rocket1;
    private Rocket rocket2;

    @BeforeEach
    void setUp() {
        mission = Mission.builder().name("Luna1").build();
        rocket1 = Rocket.builder().name("Dragon A").status(RocketStatus.OnGround).build();
        rocket2 = Rocket.builder().name("Dragon B").status(RocketStatus.InSpace).build();
    }

    @Test
    void addMission_setsScheduledStatusAndStoresMission() {
        missionService.addMission(mission);

        assertEquals(MissionStatus.Scheduled, mission.getStatus());
        assertTrue(missionService.getMissionsSummary().contains(mission));
    }

    @Test
    void changeMissionStatusToEnded_removesRocketsAndNullifiesTheirMissions() {
        assignRocketsToMission(mission, rocket1, rocket2);

        missionService.changeMissionStatus(mission, MissionStatus.Ended);

        assertEquals(MissionStatus.Ended, mission.getStatus());
        assertTrue(mission.getRockets().isEmpty());
        assertNull(rocket1.getMission());
        assertNull(rocket2.getMission());
    }

    @Test
    void changeMissionStatusToInProgress_updatesOnlyStatus() {
        missionService.changeMissionStatus(mission, MissionStatus.InProgress);

        assertEquals(MissionStatus.InProgress, mission.getStatus());
    }

    @Test
    void getMissionsSummary_returnsMissionsSortedByRocketCountAndName() {
        Mission m1 = createMission("Alpha");
        Mission m2 = createMission("Beta");

        m1.addRocket(createRocket("R1", RocketStatus.OnGround));
        m2.addRocket(createRocket("R1", RocketStatus.OnGround));
        m2.addRocket(createRocket("R2", RocketStatus.OnGround));

        missionService.addMission(m1);
        missionService.addMission(m2);

        List<Mission> summary = missionService.getMissionsSummary();

        assertEquals(List.of(m2, m1), summary);
    }

    @Test
    void getMissionsSummaryByNumberOfRockets_returnsFormattedString() {
        Mission m = createMission("Mars", MissionStatus.InProgress);
        Rocket r = createRocket("DragonX", RocketStatus.InSpace);

        m.addRocket(r);
        r.setMission(m);
        missionService.addMission(m);

        String result = missionService.getMissionsSummaryByNumberOfRockets();

        assertTrue(result.contains("• Mars - InProgress - Dragons: 1"));
        assertTrue(result.contains("o DragonX - In space"));
    }

    @Test
    void testMissionSummaryOrdering() {
        Mission m1 = createMission("Mars", MissionStatus.Scheduled);
        Mission m2 = createMission("Luna1", MissionStatus.Pending, Set.of(
                createRocket("Dragon 1", RocketStatus.OnGround),
                createRocket("Dragon 2", RocketStatus.OnGround)
        ));
        Mission m3 = createMission("Double Landing", MissionStatus.Ended);
        Mission m4 = createMission("Transit", MissionStatus.InProgress, Set.of(
                createRocket("Red Dragon", RocketStatus.OnGround),
                createRocket("Dragon XL", RocketStatus.InSpace),
                createRocket("Falcon Heavy", RocketStatus.InSpace)
        ));
        Mission m5 = createMission("Luna2", MissionStatus.Scheduled);
        Mission m6 = createMission("Vertical Landing", MissionStatus.Ended);

        missionService.addMission(m1);
        missionService.addMission(m2);
        missionService.addMission(m3);
        missionService.addMission(m4);
        missionService.addMission(m5);
        missionService.addMission(m6);

        String expected = """
                • Transit - InProgress - Dragons: 3
                o Dragon XL - In space
                o Falcon Heavy - In space
                o Red Dragon - On ground
                • Luna1 - Pending - Dragons: 2
                o Dragon 1 - On ground
                o Dragon 2 - On ground
                • Vertical Landing - Ended - Dragons: 0
                • Mars - Scheduled - Dragons: 0
                • Luna2 - Scheduled - Dragons: 0
                • Double Landing - Ended - Dragons: 0
                """;

        assertEquals(expected, missionService.getMissionsSummaryByNumberOfRockets());
    }

    // ---------- Helper Methods ----------

    private Mission createMission(String name) {
        return Mission.builder().name(name).build();
    }

    private Mission createMission(String name, MissionStatus status) {
        return Mission.builder().name(name).status(status).build();
    }

    private Mission createMission(String name, MissionStatus status, Set<Rocket> rockets) {
        return Mission.builder().name(name).status(status).rockets(rockets).build();
    }

    private Rocket createRocket(String name, RocketStatus status) {
        return Rocket.builder().name(name).status(status).build();
    }

    private void assignRocketsToMission(Mission mission, Rocket... rockets) {
        for (Rocket r : rockets) {
            mission.addRocket(r);
            r.setMission(mission);
        }
    }
}