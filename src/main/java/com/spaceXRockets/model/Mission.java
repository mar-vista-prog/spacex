package com.spaceXRockets.model;

import com.spaceXRockets.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Getter
@Builder
public class Mission {

    @NonNull
    private final String name;

    @Builder.Default
    @Setter
    @NonNull
    private MissionStatus status = MissionStatus.Scheduled;

    @Builder.Default
    private final Set<Rocket> rockets = new LinkedHashSet<>();

    public void addRocket(Rocket rocket) {
        if (rocket != null) {
            rockets.add(rocket);
        }
    }

    public void clearRockets() {
        rockets.clear();
    }

    public Set<Rocket> getRockets() {
        return Collections.unmodifiableSet(
                rockets.stream()
                        .sorted(Comparator.comparing(Rocket::getName))
                        .collect(LinkedHashSet::new, Set::add, Set::addAll)
        );
    }
}