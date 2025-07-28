package com.spaceXRockets.service.impl;

import com.spaceXRockets.enums.RocketStatus;
import com.spaceXRockets.model.*;
import com.spaceXRockets.repository.MissionRepository;
import com.spaceXRockets.enums.MissionStatus;
import com.spaceXRockets.repository.RocketRepository;
import com.spaceXRockets.service.MissionService;
import com.spaceXRockets.service.MissionStatusUpdater;
import com.spaceXRockets.service.formatter.MissionFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MissionServiceImpl implements MissionService {

    @Autowired
    private final MissionRepository missionRepository;
    @Autowired
    private final RocketRepository rocketRepository;
    private final MissionStatusUpdater missionStatusUpdater;

    public MissionServiceImpl(MissionRepository missionRepository,
                              RocketRepository rocketRepository,
                              MissionStatusUpdater missionStatusUpdater) {
        this.missionRepository = missionRepository;
        this.rocketRepository = rocketRepository;
        this.missionStatusUpdater = missionStatusUpdater;
    }

    @Override
    public void addMission(Mission mission) {
        missionRepository.add(mission);
    }

    @Override
    public void addRocket(Rocket rocket) {
        rocket.setStatus(RocketStatus.OnGround);
        rocketRepository.add(rocket);
    }

    @Override
    public void assignRocketToMission(Rocket rocket, Mission mission) {
        if (rocket.getMission() != null || mission.getStatus() == MissionStatus.Ended) {
            return;
        }

        rocket.setMission(mission);
        mission.addRocket(rocket);
        missionStatusUpdater.update(mission);
    }

    @Override
    public void assignRocketsToMission(Set<Rocket> rockets, Mission mission) {
        rockets.forEach(r -> assignRocketToMission(r, mission));
    }

    @Override
    public void changeMissionStatus(Mission mission, MissionStatus status) {
        if (status == MissionStatus.Ended) {
            mission.getRockets().forEach(r -> r.setMission(null));
            mission.clearRockets();
        }
        mission.setStatus(status);
    }

    @Override
    public void changeRocketStatus(Rocket rocket, RocketStatus status) {
        rocket.setStatus(status);
        Mission mission = rocket.getMission();
        if (mission != null) {
            if (status == RocketStatus.InRepair) {
                mission.setStatus(MissionStatus.Pending);
            }
            missionStatusUpdater.update(mission);
        }
    }

    @Override
    public List<Mission> getMissionsSummary() {
        return missionRepository.getAll().stream()
                .sorted(Comparator
                        .comparingInt((Mission m) -> m.getRockets().size()).reversed()
                        .thenComparing(Mission::getName, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public String getMissionsSummaryByNumberOfRockets() {
        return getMissionsSummary().stream()
                .map(MissionFormatter::format)
                .collect(Collectors.joining());
    }
}