package com.spaceXRockets.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@Builder
public class Rocket {

    public static final Comparator<Rocket> REVERSED_STATUS_NAME_COMPARATOR =
            Comparator
                    .comparing(Rocket::getStatus, Comparator.reverseOrder())
                    .thenComparing(Rocket::getName);

    @NonNull
    @Setter
    private final String name;

    @Builder.Default
    @NonNull
    private RocketStatus status = RocketStatus.OnGround;

    private Mission mission;

    @Override
    public String toString() {
        return "o " + name + " - " + status.getDescription() + "\n";
    }
}