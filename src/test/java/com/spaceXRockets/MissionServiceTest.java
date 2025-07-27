package com.spaceXRockets;

import com.spaceXRockets.api.MissionStatus;
import com.spaceXRockets.api.RocketStatus;
import com.spaceXRockets.model.Mission;
import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.impl.MissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MissionServiceImplTest {
    @InjectMocks
    private MissionServiceImpl missionService;

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
        List<Mission> result = missionService.getMissionsSummary();
        assertTrue(result.contains(mission));
    }

    @Test
    void changeMissionStatusToEnded_removesRocketsAndNullifiesTheirMissions() {
        rocket1.setMission(mission);
        rocket2.setMission(mission);
        mission.addRocket(rocket1);
        mission.addRocket(rocket2);

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
        Mission m1 = Mission.builder().name("Alpha").build();
        Mission m2 = Mission.builder().name("Beta").build();
        m1.addRocket(Rocket.builder().name("R1").build());
        m2.addRocket(Rocket.builder().name("R1").build());
        m2.addRocket(Rocket.builder().name("R2").build());

        missionService.addMission(m1);
        missionService.addMission(m2);

        List<Mission> summary = missionService.getMissionsSummary();

        assertEquals(List.of(m2, m1), summary); // m2 has more rockets
    }

    @Test
    void getMissionsSummaryByNumberOfRockets_returnsFormattedString() {
        Mission m = Mission.builder()
                .name("Mars")
                .status(MissionStatus.InProgress)
                .build();
        Rocket r = Rocket.builder()
                .name("DragonX")
                .status(RocketStatus.InSpace)
                .build();
        m.addRocket(r);
        r.setMission(m);
        missionService.addMission(m);

        String result = missionService.getMissionsSummaryByNumberOfRockets();

        assertTrue(result.contains("• Mars - InProgress - Dragons: 1"));
        assertTrue(result.contains("o DragonX - In space"));
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

        assertEquals("""
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
                """, result);
    }
}

