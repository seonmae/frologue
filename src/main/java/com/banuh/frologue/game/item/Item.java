package com.banuh.frologue.game.item;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.entity.Hitbox;

public class Item extends Entity {
    String name;

    public Item(String name, double x, double y, Game game) {
        super(game.getSprite("item-"+name), x, y, game, new Hitbox(0, 0, 16, 16));
    }
}
