package com.banuh.frologue.game.frog;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Hitbox;

public class ManFrog extends Frog {
    public ManFrog(double x, double y, Game game) {
        super("man", x, y, game);
        hitbox = new Hitbox(49, 50, 14, 30);
    }
}
