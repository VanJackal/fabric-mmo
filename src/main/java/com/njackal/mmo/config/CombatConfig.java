package com.njackal.mmo.config;

import java.util.Map;

public record CombatConfig(
        Map<String, Integer> weaponTypes
) {
}
