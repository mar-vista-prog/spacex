package com.spaceXRockets.repository;

import com.spaceXRockets.model.Rocket;

import java.util.List;

public interface RocketRepository {
    void add(Rocket rocket);
    List<Rocket> getAll();
}
