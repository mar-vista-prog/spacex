package com.spaceXRockets.service.formatter;

import com.spaceXRockets.model.Rocket;

public class RocketFormatter {

    public static String format(Rocket rocket) {
        return String.format("o %s - %s%n",
                rocket.getName(),
                rocket.getStatus().getDescription());
    }
}