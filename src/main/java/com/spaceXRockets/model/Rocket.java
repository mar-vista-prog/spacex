package com.spaceXRockets.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Builder
public class Rocket {
    @NonNull
    private final String name;

    @Builder.Default
    @NonNull
    @Setter
    private RocketStatus status = RocketStatus.OnGround;

    @Setter
    private Mission mission;
}