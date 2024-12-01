package com.banuh.frologue.game.entity;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Hitbox;

public class OxFrog extends Frog {
    public OxFrog(double x, double y, Game game) {
        super("ox", x, y, game);
        hitbox = new Hitbox(20, 26, 24, 14);
        SPEED = 10;
    }
}
