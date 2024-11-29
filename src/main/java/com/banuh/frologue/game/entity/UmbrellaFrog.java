package com.banuh.frologue.game.entity;

import com.banuh.frologue.core.Game;

public class UmbrellaFrog extends Frog {
    public UmbrellaFrog(double x, double y, Game game) {
        super("umbrella", x, y, game);

        JUMP_STRENGTH = 120;
    }
}
