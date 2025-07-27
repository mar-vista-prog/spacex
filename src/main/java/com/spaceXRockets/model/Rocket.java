package com.spaceXRockets.model;

import com.spaceXRockets.api.RocketStatus;
import lombok.Builder;
import lombok.Getter;
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

    @Builder.Default
    private final String name = "";

    @Builder.Default
    private RocketStatus status = RocketStatus.OnGround;

    private Mission mission;

    @Override
    public String toString() {
        return "o " + name + " - " + status.getDescription() + "\n";
    }
}