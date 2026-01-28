package com.njackal.mmo.logic;

import com.njackal.mmo.FabricMMO;
import com.njackal.mmo.persistence.DatabaseException;
import com.njackal.mmo.persistence.MMODatabase;
import com.njackal.mmo.persistence.NotificationMode;

import java.util.UUID;

public class PlayerConfigHandler {
    private final MMODatabase database;

    public PlayerConfigHandler(MMODatabase database) {
        this.database = database;
    }

    /**
     * set notification mode for a player
     * @param player id of player to set for
     * @param mode  mode to set to
     */
    public void setNotificationMode(UUID player, NotificationMode mode ) {
        database.setNotifMode(player, mode);
    }

    /**
     * set xp bar visibility for a player
     * @param player player to set for
     */
    public void setXPBarVisibility(UUID player, boolean visible) {
        database.setXpBarEnabled(player, visible);
    }

    /**
     * get xp bar visibility for a player
     * @param player player to get for
     * @return return boolean isVisible
     */
    public boolean getXPBarVisibility(UUID player) {
        try {
            return database.getXpBarEnabled(player);
        } catch (DatabaseException e) {
            FabricMMO.LOGGER.error(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get notification mode for a player
     *
     * @param player player to get for
     * @return return notification mode for the player
     */
    public NotificationMode getNotificationMode(UUID player) {
        try {
            return database.getNotifMode(player);
        } catch (DatabaseException e) {
            FabricMMO.LOGGER.error(e.getMessage());
            e.printStackTrace();
            return NotificationMode.Disabled;
        }
    }
}

