package com.banuh.frologue.core.tilemap;

import java.util.List;

public class TileMapData {
    private List<Layer> layers;
    private int mapWidth;
    private int mapHeight;
    private int tileSize;

    public TileMapData() {}

    public static class Layer {
        public String name;
        public boolean collider;
        public List<Tile> tiles;
    }

    public static class Tile {
        public String id;
        public int x;
        public int y;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getTileSize() {
        return tileSize;
    }

    public List<Layer> getLayers() {
        return layers;
    }
}
