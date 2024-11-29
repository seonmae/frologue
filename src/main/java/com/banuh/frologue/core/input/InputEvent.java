package com.banuh.frologue.core.input;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.scene.GameScene;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;

public class InputEvent <T extends Event> {
    private Scene fxscene;
    private Game game;
    private GameScene scene;
    private EventType<T> eventType;
    private EventHandler<T> eventHandler;
    private boolean available = true;

    public InputEvent(Game game, GameScene scene, EventType<T> eventType, EventHandler<T> eventHandler) {
        this.game = game;
        this.eventType = eventType;
        this.eventHandler = eventHandler;
        this.scene = scene;
        this.fxscene = game.getFxscene();
    }

    /** if scene is null, it's global event handler */
    public InputEvent(Game game, EventType<T> eventType, EventHandler<T> eventHandler) {
        this.game = game;
        this.eventType = eventType;
        this.eventHandler = eventHandler;
        this.scene = null;
        this.fxscene = game.getFxscene();
    }

    public void apply() {
        this.fxscene.addEventHandler(eventType, eventHandler);
    }

    public void cancel() {
        this.fxscene.removeEventHandler(eventType, eventHandler);
    }
}
