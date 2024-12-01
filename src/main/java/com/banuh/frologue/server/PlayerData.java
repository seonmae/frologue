package com.banuh.frologue.server;

import com.banuh.frologue.core.utils.Vector2D;

public class PlayerData {
    String pid;
    Vector2D pos;
    boolean isNew = false;
    String type;
    String spriteName;

    public PlayerData(String pid, Vector2D pos, boolean isNew, String type, String spriteName) {
        this.pid = pid;
        this.pos = pos;
        this.type = type;
        this.spriteName = spriteName;
    }
}
