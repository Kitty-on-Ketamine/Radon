package me.kitty.radon.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Key {
    private static final Map<ConfigScreen, List<String>> keys = new HashMap<>();
    static Key of(ConfigScreen screen, String key) {
        List<String> keys = Key.keys.computeIfAbsent(screen, k -> new ArrayList<>());
        if (keys.contains(key)) return null;
        Key k = new Key(key);
        keys.add(key);
        return k;
    }

    private final String key;

    private Key (String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
