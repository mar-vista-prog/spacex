package com.spaceXRockets.service.impl;

import com.spaceXRockets.model.*;
import com.spaceXRockets.service.MissionStatusUpdater;

public class MissionStatusUpdaterImpl implements MissionStatusUpdater {

    public void update(Mission mission) {
        if (mission == null) return;

        boolean hasRepair = mission.getRockets().stream()
                .anyMatch(r -> r.getStatus() == RocketStatus.InRepair);

        if (mission.getRockets().isEmpty()) {
            mission.setStatus(MissionStatus.Scheduled);
        } else if (hasRepair) {
            mission.setStatus(MissionStatus.Pending);
        } else {
            mission.setStatus(MissionStatus.InProgress);
        }
    }
}