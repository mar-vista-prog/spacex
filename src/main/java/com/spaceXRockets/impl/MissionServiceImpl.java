package com.spaceXRockets.impl;

import com.spaceXRockets.api.MissionService;
import com.spaceXRockets.api.MissionStatus;
import com.spaceXRockets.api.RocketStatus;
import com.spaceXRockets.model.Mission;
import com.spaceXRockets.model.Rocket;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MissionServiceImpl implements MissionService {

    private final Set<Mission> missions = new HashSet<>();
    private final Set<Rocket> rockets = new HashSet<>();

    @Override
    public void addMission(Mission mission) {
        missions.add(mission);
    }


    @Override
    public void assignRocketToMission(Rocket rocket, Mission mission) {
        if (rocket.getMission() != null) return;
        if (mission.getStatus() == MissionStatus.Ended) return;

        rocket.setMission(mission);
        mission.addRocket(rocket);
        updateMissionStatus(mission);
    }

    @Override
    public void assignRocketsToMission(Set<Rocket> rockets, Mission mission) {
        for (Rocket rocket : rockets) {
            assignRocketToMission(rocket, mission);
        }
    }

    @Override
    public void changeMissionStatus(Mission mission, MissionStatus missionStatus) {
        if (missionStatus == MissionStatus.Ended) {
            for (Rocket rocket : mission.getRockets()) {
                rocket.setMission(null);
            }
            mission.clearRockets();
        }
        mission.setStatus(missionStatus);
    }

    @Override
    public List<Mission> getMissionsSummary() {
        return missions.stream()
                .sorted(
                        Comparator.comparingInt((Mission m) -> m.getRockets().size()).reversed()
                                .thenComparing(Mission::getName, Comparator.reverseOrder())
                )
                .collect(Collectors.toList());
    }

    @Override
    public String getMissionsSummaryByNumberOfRockets() {
        return getMissionsSummary().stream()
                .map(Mission::toString)
                .collect(Collectors.joining());
    }


    @Override
    public void addRocket(Rocket rocket) {
        rocket.setStatus(RocketStatus.OnGround);
        rockets.add(rocket);
    }

    @Override
    public void changeRocketStatus(Rocket rocket, RocketStatus rocketStatus) {
        rocket.setStatus(rocketStatus);
        if (rocketStatus == RocketStatus.InRepair && rocket.getMission() != null) {
            rocket.getMission().setStatus(MissionStatus.Pending);
        }
        updateMissionStatus(rocket.getMission());
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