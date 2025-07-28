package com.spaceXRockets.service.formatter;

import com.spaceXRockets.model.Mission;

import java.util.stream.Collectors;

public class MissionFormatter {

    public static String format(Mission mission) {
        String header = String.format("• %s - %s - Dragons: %d%n",
                mission.getName(),
                mission.getStatus(),
                mission.getRockets().size());

        String rockets = mission.getRockets().stream()
                .map(RocketFormatter::format)
                .collect(Collectors.joining());

        return header + rockets;
    }
}