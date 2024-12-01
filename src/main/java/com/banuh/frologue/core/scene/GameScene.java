package com.banuh.frologue.core.scene;

import com.banuh.frologue.core.Game;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.input.InputEvent;
import com.banuh.frologue.core.tilemap.OverLap;
import com.banuh.frologue.core.tilemap.PlacedTileMap;
import com.banuh.frologue.core.utils.Vector2D;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;

public class GameScene {
    private Runnable startCallback = () -> {};
    private Runnable endCallback = () -> {};

    public String name;
    public Game game;
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<InputEvent> eventList = new ArrayList<>();
    public ArrayList<PlacedTileMap> placedTilemapList = new ArrayList<>();

    public GameScene(Game game, String name) {
        this.game = game;
        this.name = name;
    }

    public GameScene(Game game, String name, Runnable start) {
        this.game = game;
        this.name = name;
        this.startCallback = start;
    }

    public GameScene(Game game, String name, Runnable start, Runnable end) {
        this.game = game;
        this.name = name;
        if (start != null) {
            this.startCallback = start;
        }
        this.endCallback = end;
    }

    public void start() {

    }

    public void end() {

    }

    public void defaultStart() {
        for (InputEvent event: eventList) {
            event.apply();
        }
    }

    public void update() {

    }

    public void render() {

    }

    public void defaultEnd() {
        for (InputEvent event: eventList) {
            event.cancel();
        }
    }

    public <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<T> eventHandler) {
        InputEvent<T> event = new InputEvent<>(game, this, eventType, eventHandler);
        eventList.add(event);
    }

    public Runnable getStartCallback() {
        return startCallback;
    }

    public Runnable getEndCallback() {
        return endCallback;
    }

    public void runStartCallback() {
        start();
        startCallback.run();
        defaultStart();
    }

    public void runEndCallback() {
        end();
        endCallback.run();
        defaultEnd();
    }

    public OverLap isCollision(Vector2D pos, double width, double height) {
        for (PlacedTileMap map: placedTilemapList) {
            OverLap collision = map.isCollision("solider", pos, width, height);
            if (collision.is) return collision;
        }
        return new OverLap(false);
    }
}
