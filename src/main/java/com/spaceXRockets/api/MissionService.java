package com.spaceXRockets.api;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.model.Rocket;

import java.util.List;
import java.util.Set;

public interface MissionService {

    void addMission(Mission mission);

    void assignRocketToMission(Rocket rocket, Mission mission);

    void assignRocketsToMission(Set<Rocket> rockets, Mission mission);

    void changeMissionStatus(Mission mission, MissionStatus missionStatus);

    List<Mission> getMissionsSummary();

    String getMissionsSummaryByNumberOfRockets();

    void addRocket(Rocket rocket);

    void changeRocketStatus(Rocket rocket, RocketStatus rocketStatus);
}