package com.spaceXRockets.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class Mission {

    @NonNull
    @Setter
    private final String name;

    @Builder.Default
    @Setter
    @NonNull
    private MissionStatus status = MissionStatus.Scheduled;

    @Builder.Default
    private final Set<Rocket> rockets = new HashSet<>();

    public void addRocket(Rocket rocket) {
        if (rocket != null) {
            rockets.add(rocket);
        }
    }

    public void clearRockets() {
        rockets.clear();
    }

    public Set<Rocket> getRockets() {
        return Collections.unmodifiableSet(rockets);
    }

    @Override
    public String toString() {
        return "• " + name + " - " + status + " - Dragons: " + rockets.size() + "\n" +
                rockets.stream()
                        .sorted(Rocket.REVERSED_STATUS_NAME_COMPARATOR)
                        .map(Rocket::toString)
                        .collect(Collectors.joining());
    }
}