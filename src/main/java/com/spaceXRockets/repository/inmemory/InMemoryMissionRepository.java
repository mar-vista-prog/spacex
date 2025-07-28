package com.spaceXRockets.repository.inmemory;

import com.spaceXRockets.model.Mission;
import com.spaceXRockets.repository.MissionRepository;

import java.util.*;

public class InMemoryMissionRepository implements MissionRepository {
    private final Set<Mission> missions = new HashSet<>();

    public void add(Mission mission) {
        missions.add(mission);
    }

    public List<Mission> getAll() {
        return new ArrayList<>(missions);
    }
}