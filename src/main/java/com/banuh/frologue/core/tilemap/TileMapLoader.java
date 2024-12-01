package com.banuh.frologue.core.tilemap;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TileMapLoader {
    public static TileMapData load(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream jsonStream = TileMapLoader.class.getResourceAsStream(path);
        TileMapData tilemap = mapper.readValue(jsonStream, TileMapData.class);

        return tilemap;
    }
}
