package com.banuh.frologue.game.entity;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.entity.Hitbox;

public abstract class Frog extends Entity {
    public String type;
    public double JUMP_STRENGTH = 200;
    public double SPEED = 50;

    public Frog(String type, double x, double y, Game game) {
        super(game.getSprite("frog-"+type+"-idle"),
            x, y, game, new Hitbox(23, 28, 19, 12)
        );

        this.type = type;

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
