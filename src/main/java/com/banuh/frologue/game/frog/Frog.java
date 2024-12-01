package com.banuh.frologue.game.frog;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.entity.Hitbox;

public abstract class Frog extends Entity {
    public String type;
    public double JUMP_STRENGTH = 100;
    public double JUMP_METER = 100;
    public double jump_scale = 1;
    public double SPEED = 30;
    private boolean onGround = false;
    private boolean inSky = false;

    public Frog(String type, double x, double y, Game game) {
        super(game.getSprite("frog-"+type+"-idle"),
            x, y, game, new Hitbox(24, 28, 16, 12)
        );

        this.type = type;

        addState("move", "frog-"+type+"-move");
        addState("jump", "frog-"+type+"-jump");
        addState("up", "frog-"+type+"-jump");
        addState("fall", "frog-"+type+"-fall");
        addState("land", "frog-"+type+"-land");
        addState("hurt", "frog-"+type+"-hurt");
        addState("turn", "frog-"+type+"-turn");
        addState("death", "frog-"+type+"-death");
        addState("charging", "frog-"+type+"-charging");
        addState("charged", "frog-"+type+"-charged");

        addVelocity("move");
        addVelocity("wall");
        addVelocity("move_with_jump");
        addVelocity("gravity");
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isInSky() {
        return inSky;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setInSky(boolean inSky) {
        this.inSky = inSky;
    }
}
