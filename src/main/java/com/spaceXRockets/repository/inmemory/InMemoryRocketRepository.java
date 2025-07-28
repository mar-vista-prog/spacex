package com.spaceXRockets.repository.inmemory;

import com.spaceXRockets.model.Rocket;
import com.spaceXRockets.repository.RocketRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryRocketRepository implements RocketRepository {
    private final Set<Rocket> rockets = new HashSet<>();

    public void add(Rocket rocket) {
        rockets.add(rocket);
    }

    public List<Rocket> getAll() {
        return new ArrayList<>(rockets);
    }
}