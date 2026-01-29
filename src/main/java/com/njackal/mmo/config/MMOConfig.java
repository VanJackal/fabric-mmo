package com.njackal.mmo.config;


public record MMOConfig(
        BlockBreakConfig woodcutting,
        BlockBreakConfig mining,
        BlockBreakConfig excavation,
        BlockBreakConfig herbalism,
        FishingConfig fishing,
        int acrobaticsMultiplier
) {
}
