package me.kitty.radon.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.kitty.radon.Screens.ModMenu;

import java.util.HashMap;
import java.util.Map;

import static me.kitty.radon.client.RadonClient.screens;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> map = new HashMap<>(ModMenuApi.super.getProvidedConfigScreenFactories());
        for (String modId : screens.keySet()) {
            map.put(modId, parent -> screens.get(modId).setParent(parent).fromTop());
        }
        return map;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenu::new;
    }
}