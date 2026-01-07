package com.njackal.mmo.event;

import com.njackal.mmo.persistence.XPType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class PlayerEventHandler {
    protected final List<PlayerXPEvent> observers;

    public PlayerEventHandler() {
        observers = new LinkedList<>();
    }

    public void observe(PlayerXPEvent xpEventHandler) {
        observers.add(xpEventHandler);
    }
    protected void fireXpEvent(XPType xpType, int xp, UUID player){
        for  (PlayerXPEvent observer : observers) {
            observer.gainXP(xpType, xp, player);
        }
    }
}
