package com.spaceXRockets.service;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.enums.MissionStatus;
import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.enums.RocketStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MissionStatusUpdaterTest {

    private final com.spaceXRockets.service.MissionStatusUpdater updater = new com.spaceXRockets.service.impl.MissionStatusUpdaterImpl();

    @Test
    void updateScheduledWhenNoRockets() {
        Mission mission = Mission.builder().name("Solo").build();

        updater.update(mission);

        assertEquals(MissionStatus.Scheduled, mission.getStatus());
    }

    @Test
    void updatePendingWhenRocketInRepair() {
        Mission mission = Mission.builder().name("Repair").build();
        mission.addRocket(Rocket.builder().name("R1").status(RocketStatus.InRepair).build());

        updater.update(mission);

        assertEquals(MissionStatus.Pending, mission.getStatus());
    }

    @Test
    void updateInProgressWhenAllRocketsActive() {
        Mission mission = Mission.builder().name("Active").build();
        mission.addRocket(Rocket.builder().name("R1").status(RocketStatus.OnGround).build());
        mission.addRocket(Rocket.builder().name("R2").status(RocketStatus.InSpace).build());

        updater.update(mission);

        assertEquals(MissionStatus.InProgress, mission.getStatus());
    }
}