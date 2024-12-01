package com.banuh.frologue.game.item;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.entity.Hitbox;
import com.banuh.frologue.game.frog.Frog;

public class EnergyDrink extends Item {
    String name;

    public EnergyDrink(double x, double y, Game game) {
        super("energy_drink", x, y, game);
    }

    public void use(Frog frog) {
        frog.setFlag("energy_drink");
        game.setTimeout(() -> {
            frog.setFlag("energy_drink", false);
        }, 10000);
    };
}
