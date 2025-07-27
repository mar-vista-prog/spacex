package com.spaceXRockets.api;

public enum RocketStatus {
    OnGround("On ground"),
    InSpace("In space"),
    InRepair("In repair");

    private final String description;

    RocketStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}