package com.njackal.mmo.config;

import java.util.Map;

public record MMOConfig(
        BlockBreakConfig woodcutting,
        BlockBreakConfig mining,
        BlockBreakConfig excavation,
        BlockBreakConfig herbalism,
        FishingConfig fishing
) {
}
