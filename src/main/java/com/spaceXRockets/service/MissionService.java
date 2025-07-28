package com.spaceXRockets.service;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.model.MissionStatus;
import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.model.RocketStatus;

import java.util.List;
import java.util.Set;

public interface MissionService {
    void addMission(Mission mission);

    void addRocket(Rocket rocket);

    void assignRocketToMission(Rocket rocket, Mission mission);

    void assignRocketsToMission(Set<Rocket> rockets, Mission mission);

    void changeMissionStatus(Mission mission, MissionStatus status);

    void changeRocketStatus(Rocket rocket, RocketStatus status);

    List<Mission> getMissionsSummary();

    String getMissionsSummaryByNumberOfRockets();
}
