package com.banuh.frologue.game.frog;

import com.banuh.frologue.core.Game;

public class NinjaFrog extends Frog {
    public NinjaFrog(double x, double y, Game game) {
        super("ninja", x, y, game);
        JUMP_METER = 100;
        JUMP_STRENGTH = 100;
        SPEED = 250;
    }
}
