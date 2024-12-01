package com.banuh.frologue.core;

import com.banuh.frologue.core.camera.GameCamera;
import com.banuh.frologue.core.entity.Entity;
import com.banuh.frologue.core.entity.Hitbox;
import com.banuh.frologue.core.input.InputEvent;
import com.banuh.frologue.core.scene.GameScene;
import com.banuh.frologue.core.sprite.AnimationSprite;
import com.banuh.frologue.core.sprite.SimpleSprite;
import com.banuh.frologue.core.sprite.Sprite;
import com.banuh.frologue.core.tilemap.PlacedTileMap;
import com.banuh.frologue.core.tilemap.TileMap;
import com.banuh.frologue.core.utils.Schedule;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public abstract class Game {
  private double updateInterval = 1_000_000_000.0 / 60 / 2;
  private double renderInterval = 1_000_000_000.0 / 60;
  private double fps = 60;
  public GameCamera camera;
  public GraphicsContext gc;
  private Canvas canvas;
  private Scene fxscene;
  public final int width;
  public final int height;
  public KeyBoardIsPressed isPressed = new KeyBoardIsPressed();
  public boolean showHitbox = false;
  public Color backgroundColor = Color.WHITE;

  private HashMap<String, GameScene> sceneList = new HashMap<>();
  public HashMap<String, TileMap> tileMapList = new HashMap<>();
  private GameScene currentScene;
  public ArrayList<InputEvent> eventList = new ArrayList<>();

  private HashMap<String, Sprite> spriteMap = new HashMap<>();
  public long runTime = 0;
  public ArrayList<Schedule> timeoutSchedules = new ArrayList<>();
  public ArrayList<Schedule> intervalSchedules = new ArrayList<>();
  public HashMap<String, AudioClip> soundList = new HashMap<>();

  public Game(Canvas canvas, int width, int height, GameScene firstScene) {
    this.canvas = canvas;
    this.gc = canvas.getGraphicsContext2D();
    this.width = width;
    this.height = height;

    this.camera = new GameCamera(0, 0, 1);

    addScene(firstScene);
    setCurrentScene(firstScene.name);

    fxscene = canvas.getScene();
  }

  public Game(Canvas canvas, int width, int height, String firstSceneName) {
    this.canvas = canvas;
    this.gc = canvas.getGraphicsContext2D();
    this.width = width;
    this.height = height;

    this.camera = new GameCamera(0, 0, 1);

    addScene(firstSceneName);
    setCurrentScene(firstSceneName);

    fxscene = canvas.getScene();
  }

  public Game(Canvas canvas, int width, int height) {
    this.canvas = canvas;
    this.gc = canvas.getGraphicsContext2D();
    this.width = width;
    this.height = height;

    this.camera = new GameCamera(0, 0, 1);

    addScene("start");
    setCurrentScene("start");

    fxscene = canvas.getScene();
  }

  public abstract void preload();

  public abstract void update();

  private void defaultUpdate() {
    runTime += (int)updateInterval;

    Iterator<Schedule> iter1 = timeoutSchedules.iterator();
    while (iter1.hasNext()) {
      Schedule schedule = iter1.next();
      if (schedule.endTime <= runTime) {
        schedule.apply();
        iter1.remove();
      }
    }

    Iterator<Schedule> iter2 = intervalSchedules.iterator();
    while (iter2.hasNext()) {
      Schedule schedule = iter2.next();
      if (schedule.endTime <= runTime) {
        schedule.apply();
        schedule.endTime += schedule.duration;
      }
    }

    for (Entity entity: currentScene.entityList) {
      entity.pos.added(entity.getTotalVelocity().divide(fps));
    }
  }

  public abstract void render();

  private void defaultRender() {
    gc.setFill(backgroundColor);
    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
//    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (PlacedTileMap tileMap: currentScene.placedTilemapList) {
      tileMap.draw();
    }

    for (Entity entity: currentScene.entityList) {
      entity.draw(gc, camera);
      if (showHitbox) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeRect(
                (entity.pos.getX() - camera.pos.getX()) * camera.scale,
                (entity.pos.getY() - camera.pos.getY()) * camera.scale,
                entity.getWidth() * camera.scale,
                entity.getHeight() * camera.scale
        );
      }
    }
  }

  public void defaultStart() {
    for (InputEvent event: eventList) {
      event.apply();
    }
  }

  public abstract void start();

  public Sprite addSprite(String name, String src) {
    Sprite sprite = new SimpleSprite(src, name);

    spriteMap.put(name, sprite);
    return sprite;
  }

  public Sprite addSprite(String name, String src, int row, int col, int fps) {
    int frameLength = row * col;

    Sprite sprite = new AnimationSprite(src, row, col, frameLength, 1000.0 / fps, name);

    spriteMap.put(name, sprite);
    return sprite;
  }

  public Sprite addSprite(String name, String src, int row, int col, int frameLength, int fps) {
    src = src;
    Sprite sprite = new AnimationSprite(src, row, col, frameLength, 1000.0 / fps, name);

    spriteMap.put(name, sprite);
    return sprite;
  }

  public Sprite getSprite(String name) {
    return spriteMap.get(name);
  }

  public Entity addEntity(String spriteName, double x, double y, Hitbox hitbox) {
    if (spriteMap.containsKey(spriteName)) {
      Sprite sprite = spriteMap.get(spriteName);
      Entity entity = new Entity(sprite, x, y, this, hitbox);
      currentScene.entityList.add(entity);

      return entity;
    } else {
      System.out.println("찾을 수 없는 스프라이트 이름입니다: " + spriteName);
      return null;
    }
  }

  public AudioClip addSound(String name, String path) {
    try {
      AudioClip clip = new AudioClip(getClass().getResource("/" + path).toExternalForm());
      soundList.put(name, clip);

      return clip;
    } catch (Exception e) {
      System.err.println("Failed to load sound: " + name + " from /" + path);
      e.printStackTrace();

      return null;
    }
  }

  public void playSound(String name) {
    AudioClip clip = soundList.get(name);
    if (clip != null) {
      clip.play();
    } else {
      System.err.println("Sound not found: " + name);
    }
  }

  /** 사운드 멈춤 */
  public void stopSound(String name) {
    AudioClip clip = soundList.get(name);
    if (clip != null) {
      clip.stop();
    } else {
      System.err.println("Sound not found: " + name);
    }
  }

  public Entity addEntity(String spriteName, double x, double y) {
    Sprite sprite = spriteMap.get(spriteName);
    return addEntity(spriteName, x, y, new Hitbox(0, 0, sprite.width, sprite.height));
  }

  public Entity addEntity(Entity entity) {
    currentScene.entityList.add(entity);

    return entity;
  }

  public PlacedTileMap placeTileMap(String name, double x, double y) {
    PlacedTileMap tilemap = new PlacedTileMap(name, tileMapList.get(name), x, y, this);
    currentScene.placedTilemapList.add(tilemap);

    return tilemap;
  }

  public PlacedTileMap placeTileMapByBottom(String name, double x, double y) {
    TileMap tileMap = tileMapList.get(name);
    PlacedTileMap tilemap = new PlacedTileMap(name, tileMap, x, y - tileMap.getHeight(), this);
    currentScene.placedTilemapList.add(tilemap);

    return tilemap;
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

  public TileMap addTileMap(String name, String path) {
    try {
      TileMap tileMap = new TileMap(name, path, this);
      tileMapList.put(name, tileMap);
      return tileMap;
    } catch (IOException e) {
      // 적절한 예외 처리 (로그 출력, 사용자 알림 등)
      e.printStackTrace();
      return null; // 또는 적절한 기본값 반환
    }
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
    this.timeoutSchedules.add(new Schedule(runTime, duration * 1000, callback));
  }

  public void setInterval(Runnable callback, long duration) {
    this.intervalSchedules.add(new Schedule(runTime, duration * 1000, callback));
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

  public double getFps() {
    return fps;
  }

  public long FRAME() {
    return (long)renderInterval / 1000;
  }

  // start 에서 preload 나 주기 등을 관리
  public void run() {
    preload();
    gc.setImageSmoothing(false);

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