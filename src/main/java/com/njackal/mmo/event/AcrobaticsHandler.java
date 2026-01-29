package com.njackal.mmo.event;

import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public class AcrobaticsHandler extends  PlayerEventHandler{
    private final int acrobaticsXp;

    public AcrobaticsHandler(int acrobaticsXp) {
        super();
        this.acrobaticsXp = acrobaticsXp;
    }

    public void handleAcrobatics(float fallDamage, UUID player){
        fireXpEvent(XPType.Acrobatics, (int)(fallDamage*acrobaticsXp), player);
    }
}
