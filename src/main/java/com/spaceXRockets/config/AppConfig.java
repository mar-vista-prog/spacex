package com.spaceXRockets.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.spaceXRockets.config",
        "com.spaceXRockets.model",
        "com.spaceXRockets.repository",
        "com.spaceXRockets.service"
})
public class AppConfig {
}