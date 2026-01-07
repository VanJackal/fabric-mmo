package com.njackal.mmo.event;

import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public interface PlayerXPEvent {
    void gainXP(XPType type, int xp, UUID player);
}
