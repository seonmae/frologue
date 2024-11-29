package com.banuh.frologue.game.entity;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.entity.Hitbox;

public abstract class Frog extends Entity {
    public String type;
    public double JUMP_STRENGTH = 600;

    public Frog(String type, double x, double y, Game game) {
        super(game.getSprite("frog-"+type+"-idle"), x, y, game);

        this.type = type;

        hitbox = new Hitbox(23, 28, 19, 12);

        addState("move", "frog-"+type+"-move");
        addState("jump", "frog-"+type+"-jump");
        addState("fall", "frog-"+type+"-fall");
        addState("land", "frog-"+type+"-land");
        addState("hurt", "frog-"+type+"-hurt");
        addState("death", "frog-"+type+"-death");
        addState("turn", "frog-"+type+"-turn");

        addVelocity("move");
        addVelocity("gravity");
    }
}
