package me.kitty.radon.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

class Saver {
    private static final Gson gson = new Gson();
    private final Path path;

    Saver(String mod) {
        path = FabricLoader.getInstance().getConfigDir().resolve("radon").resolve(mod + ".json");
    }

    JsonObject load() {
        try {
            Files.createDirectories(path.getParent());
            if (path.toFile().exists()) {
                try (Reader reader = new FileReader(path.toFile())) {
                    return gson.fromJson(reader, JsonObject.class);
                }
            } else {
                Files.createFile(path);
                save(new JsonObject());
                return new JsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void save(JsonObject object) {
        try {
            Files.writeString(path, gson.toJson(object));
        } catch (Exception e) {}
    }

}
