package com.njackal.mmo.event;

import com.njackal.mmo.persistence.XPType;

import java.util.UUID;

public class AcrobaticsHandler extends  PlayerEventHandler{
    public static final int ACROBATICS_XP = 120; //per half heart

    public AcrobaticsHandler() {
        super();
    }

    public void handleAcrobatics(float fallDamage, UUID player){
        fireXpEvent(XPType.Acrobatics, (int)(fallDamage*ACROBATICS_XP), player);
    }
}
