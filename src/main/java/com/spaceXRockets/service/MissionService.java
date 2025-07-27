package com.spaceXRockets.service;

import com.spaceXRockets.model.MissionStatus;
import com.spaceXRockets.model.RocketStatus;
import com.spaceXRockets.model.Mission;
import com.spaceXRockets.model.Rocket;

import java.util.*;
import java.util.stream.Collectors;

public class MissionService {

    private final Set<Mission> missions = new HashSet<>();
    private final Set<Rocket> rockets = new HashSet<>();

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public void assignRocketToMission(Rocket rocket, Mission mission) {
        if (rocket.getMission() != null || mission.getStatus() == MissionStatus.Ended) {
            return;
        }

        rocket.setMission(mission);
        mission.addRocket(rocket);
        updateMissionStatus(mission);
    }

    public void assignRocketsToMission(Set<Rocket> rockets, Mission mission) {
        rockets.forEach(rocket -> assignRocketToMission(rocket, mission));
    }

    public void changeMissionStatus(Mission mission, MissionStatus missionStatus) {
        if (missionStatus == MissionStatus.Ended) {
            mission.getRockets().forEach(rocket -> rocket.setMission(null));
            mission.clearRockets();
        }
        mission.setStatus(missionStatus);
    }

    public List<Mission> getMissionsSummary() {
        return missions.stream()
                .sorted(Comparator
                        .comparingInt((Mission m) -> m.getRockets().size()).reversed()
                        .thenComparing(Mission::getName, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public String getMissionsSummaryByNumberOfRockets() {
        return getMissionsSummary().stream()
                .map(Mission::toString)
                .collect(Collectors.joining());
    }

    public void addRocket(Rocket rocket) {
        rocket.setStatus(RocketStatus.OnGround);
        rockets.add(rocket);
    }

    public void changeRocketStatus(Rocket rocket, RocketStatus rocketStatus) {
        rocket.setStatus(rocketStatus);
        Mission mission = rocket.getMission();
        if (mission != null) {
            if (rocketStatus == RocketStatus.InRepair) {
                mission.setStatus(MissionStatus.Pending);
            }
            updateMissionStatus(mission);
        }
    }

    private void updateMissionStatus(Mission mission) {
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