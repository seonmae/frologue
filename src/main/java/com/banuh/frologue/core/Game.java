package com.banuh.frologue.core;

import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.input.InputEvent;
import com.banuh.frologue.core.scene.GameScene;
import com.banuh.frologue.core.sprite.AnimationSprite;
import com.banuh.frologue.core.sprite.SimpleSprite;
import com.banuh.frologue.core.sprite.Sprite;
import com.banuh.frologue.core.utils.Schedule;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public abstract class Game {
  private double updateInterval = 1_000_000_000.0 / 60 / 2;
  private double renderInterval = 1_000_000_000.0 / 60;
  private double fps = 60;
  public double scale = 3;
  private GraphicsContext gc;
  private Canvas canvas;
  private Scene fxscene;
  private int width;
  private int height;
  public String defaultURL = "";
  public KeyBoardIsPressed isPressed = new KeyBoardIsPressed();

  private HashMap<String, GameScene> sceneList = new HashMap<>();
  private GameScene currentScene;
  public ArrayList<InputEvent> eventList = new ArrayList<>();

  private HashMap<String, Sprite> spriteMap = new HashMap<>();
  public long runTime = 0;
  public ArrayList<Schedule> schedules = new ArrayList<>();

  public Game(Canvas canvas, int width, int height, GameScene firstScene) {
    this.canvas = canvas;
    this.gc = canvas.getGraphicsContext2D();
    this.width = width;
    this.height = height;

    addScene(firstScene);
    setCurrentScene(firstScene.name);

    fxscene = canvas.getScene();
  }

  public Game(Canvas canvas, int width, int height, String firstSceneName) {
    this.canvas = canvas;
    this.gc = canvas.getGraphicsContext2D();
    this.width = width;
    this.height = height;

    addScene(firstSceneName);
    setCurrentScene(firstSceneName);

    fxscene = canvas.getScene();
  }

  public Game(Canvas canvas, int width, int height) {
    this.canvas = canvas;
    this.gc = canvas.getGraphicsContext2D();
    this.width = width;
    this.height = height;

    addScene("start");
    setCurrentScene("start");

    fxscene = canvas.getScene();
  }

  public abstract void preload();

  public abstract void update();

  private void defaultUpdate() {
    runTime += (int)updateInterval;

    Iterator<Schedule> iterator = schedules.iterator();
    while (iterator.hasNext()) {
      Schedule schedule = iterator.next();
      if (schedule.endTime <= runTime) {
        schedule.apply();
        iterator.remove();
      }
    }

    for (Entity entity: currentScene.entityList) {
      entity.pos.added(entity.getTotalVelocity().divide(fps));

/*
      for (Vector2D velocity: entity.velocity.values()) {
        velocity.set(0, 0);
      }
*/
    }
  }

  public abstract void render();

  private void defaultRender() {
    gc.clearRect(0, 0, width, height);

    for (Entity entity: currentScene.entityList) {
      entity.draw(gc, scale);
    }
  }

  public void defaultStart() {
    for (InputEvent event: eventList) {
      event.apply();
    }
  }

  public abstract void start();

  public Sprite addSprite(String name, String src) {
    src = defaultURL + src;
    Sprite sprite = new SimpleSprite(src, name);

    spriteMap.put(name, sprite);
    return sprite;
  }

  public Sprite addSprite(String name, String src, int row, int col, int fps) {
    int frameLength = row * col;

    src = defaultURL + src;
    Sprite sprite = new AnimationSprite(src, row, col, frameLength, 1000.0 / fps, name);

    spriteMap.put(name, sprite);
    return sprite;
  }

  public Sprite addSprite(String name, String src, int row, int col, int frameLength, int fps) {
    src = defaultURL + src;
    Sprite sprite = new AnimationSprite(src, row, col, frameLength, 1000.0 / fps, name);

    spriteMap.put(name, sprite);
    return sprite;
  }

  public Sprite getSprite(String name) {
    return spriteMap.get(name);
  }

  public Entity addEntity(String spriteName, double x, double y) {
    if (spriteMap.containsKey(spriteName)) {
      Sprite sprite = spriteMap.get(spriteName);
      Entity entity = new Entity(sprite, x, y, this);
      currentScene.entityList.add(entity);

      return entity;
    } else {
      System.out.println("찾을 수 없는 스프라이트 이름입니다: " + spriteName);
      return null;
    }
  }

  public Entity addEntity(Entity entity) {
    currentScene.entityList.add(entity);

    return entity;
  }

  public void addScene(GameScene newScene) {
    this.sceneList.put(newScene.name, newScene);
  }

  public void addScene(String newSceneName) {
    this.sceneList.put(newSceneName, new GameScene(this, newSceneName));
  }

  public void addScene(String newSceneName, Runnable startCallback) {
    this.sceneList.put(newSceneName, new GameScene(this, newSceneName, startCallback));
  }

  public void addScene(String newSceneName, Runnable startCallback, Runnable endCallback) {
    this.sceneList.put(newSceneName, new GameScene(this, newSceneName, startCallback, endCallback));
  }

  public void setCurrentScene(String name) {
    if (currentScene == null || !Objects.equals(currentScene.name, name)) {
      if (currentScene != null) {
        currentScene.runEndCallback();
      }
      currentScene = this.sceneList.get(name);
      currentScene.runStartCallback();
    }
  }

  public String getCurrentSceneName() {
    return currentScene.name;
  }

  public GameScene getCurrentScene() {
    return sceneList.get(getCurrentSceneName());
  }

  public <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<T> eventHandler) {
    InputEvent<T> event = new InputEvent<>(this, eventType, eventHandler);
    eventList.add(event);
  }

  public void setTimeout(Runnable callback, long duration) {
    this.schedules.add(new Schedule(runTime, duration * 1000, callback));
  }

  public double setFPS(int fps) {
    this.fps = fps;
    renderInterval = 1_000_000_000.0 / this.fps;
    updateInterval = renderInterval / 2;

    return renderInterval;
  }

  public double getUpdateInterval() {
    return updateInterval;
  }

  public double getRenderInterval() {
    return renderInterval;
  }

  public long FRAME() {
    return (long)renderInterval / 1000;
  }

  // start 에서 preload 나 주기 등을 관리
  public void run() {
    preload();

    fxscene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      switch (event.getCode()) {
        case LEFT: isPressed.leftKey = true; break;
        case RIGHT: isPressed.rightKey = true; break;
        case SPACE: isPressed.spaceKey = true; break;
      }
    });

    fxscene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
      switch (event.getCode()) {
        case LEFT: isPressed.leftKey = false; break;
        case RIGHT: isPressed.rightKey = false; break;
        case SPACE: isPressed.spaceKey = false; break;
      }
    });

    start();
    defaultStart();

    new AnimationTimer() {
      private long lastUpdate = 0;
      private long lastRender = 0;

      @Override
      public void handle(long now) {
        if ((now - lastUpdate) >= updateInterval) {
          update();
          defaultUpdate();
          currentScene.update();
          lastUpdate = now;
        }

        if ((now - lastRender >= renderInterval)) {
          render();
          defaultRender();
          currentScene.render();
          lastRender = now;
        }
      }
    }.start();
  }

  public Canvas getCanvas() {
    return canvas;
  }
  public Scene getFxscene() {
    return fxscene;
  }

  public static class KeyBoardIsPressed {
    public boolean spaceKey;
    public boolean leftKey;
    public boolean rightKey;

    public KeyBoardIsPressed() {
      spaceKey = false;
      leftKey = false;
      rightKey = false;
    }
  }
}