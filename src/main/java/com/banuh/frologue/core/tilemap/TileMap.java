package com.banuh.frologue.core.tilemap;

import com.banuh.frologue.core.Game;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.*;

public class TileMap {
    private int mapWidth;
    private int mapHeight;
    private int tileSize;
    private String name;
    private LinkedHashMap<String, Layer> hashMapLayers = new LinkedHashMap<>();
    private List<Layer> layers;
    private Image tileSet;
    private int tilesetCols;
    private Game game;

    public TileMap(String name, String path, Game game) throws IOException {
        TileMapData tileMapOriginData = TileMapLoader.load("/" + path + "/map.json");
        tileSet = new Image("file:src/main/resources/" + path + "/spritesheet.png");

        tileSize = tileMapOriginData.getTileSize();
        mapWidth = tileMapOriginData.getMapWidth();
        mapHeight = tileMapOriginData.getMapHeight();

        tilesetCols = (int)tileSet.getWidth() / tileSize;

        this.name = name;
        this.game = game;

        for (TileMapData.Layer originLayer: tileMapOriginData.getLayers()) {
            Layer layer = new Layer(originLayer.name, mapWidth, mapHeight, originLayer.collider);
            hashMapLayers.put(originLayer.name, layer);

            for (TileMapData.Tile originTile: originLayer.tiles) {
                int x = originTile.x;
                int y = originTile.y;
                layer.tiles[y][x] = new Tile(originTile.id, x, y);
            }
        }

        this.layers = new ArrayList<>(hashMapLayers.values());
        Collections.reverse(layers);
    }

    public void draw(double x, double y) {
        x -= game.camera.pos.getX();
        y -= game.camera.pos.getY();

        for (Layer layer: layers) {
            for (int yi = 0; yi < mapHeight; yi++) {
                for (int xi = 0; xi < mapWidth; xi++) {
                    Tile tile = layer.tiles[yi][xi];

                    if (tile == null) continue;

                    double dx = xi * tileSize;
                    double dy = yi * tileSize;

                    double drawX = (x + dx) * game.camera.scale;
                    double drawY = (y + dy) * game.camera.scale;

                    game.gc.drawImage(
                        tileSet, (tile.id % tilesetCols) * tileSize, ((int)tile.id / tilesetCols) * tileSize, tileSize, tileSize,
                        drawX, drawY, tileSize * game.camera.scale, tileSize * game.camera.scale
                    );
                }
            }
        }
    }

    public void printMap() {
        for (Layer layer: layers) {
            System.out.println("layer: " + layer.name);

            for (int j = 0; j < layer.tiles.length; j++) {
                for (int k = 0; k < layer.tiles[j].length; k++) {
                    if (layer.tiles[j][k] == null) {
                        System.out.printf("   ");
                    } else {
                        System.out.printf("%2d ", layer.tiles[j][k].id);
                    }
                }
                System.out.println();
            }

            System.out.println();
        }
    }

    public int getHeight() {
        return mapHeight * tileSize;
    }

    public int getWidth() {
        return mapWidth * tileSize;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public LinkedHashMap<String, Layer> getHashMapLayers() {
        return hashMapLayers;
    }

    public Layer getLayer(String name) {
        return hashMapLayers.get(name);
    }

    public int getTilesetCols() {
        return tilesetCols;
    }

    public String getName() {
        return name;
    }

    public static class Layer {
        public String name;
        public boolean collider;
        public Tile[][] tiles;

        public Layer(String name, int width, int height, boolean collider) {
            this.name = name;
            this.collider = collider;
            tiles = new Tile[height][width];
        }
    }

    public static class Tile {
        int id = 0;
        int x = 0;
        int y = 0;

        public Tile(String id, int x, int y) {
            this.id = Integer.parseInt(id);
            this.x = x;
            this.y = y;
        }

        public int getId() {
            return id;
        }
    }
}
