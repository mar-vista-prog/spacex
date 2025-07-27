package com.spaceXRockets.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.spaceXRockets.api",
        "com.spaceXRockets.impl",
        "com.spaceXRockets.config"
})
public class AppConfig {
}