package com.spaceXRockets.repository;

import com.spaceXRockets.model.Mission;

import java.util.List;

public interface MissionRepository {
    void add(Mission mission);
    List<Mission> getAll();
}
