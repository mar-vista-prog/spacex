package com.spaceXRockets.service;

import com.spaceXRockets.model.Mission;

@FunctionalInterface
public interface MissionStatusUpdater {
    void update(Mission mission);
}
