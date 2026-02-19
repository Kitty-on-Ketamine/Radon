package me.kitty.radon.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.kitty.radon.api.ConfigScreen;
import me.kitty.radon.Screens.ModMenu;
import me.kitty.radon.Widgets.WidgetDrawer;

import java.util.HashMap;
import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {
    private static final Map<String, ConfigScreen> screens = new HashMap<>();
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> map = new HashMap<>(ModMenuApi.super.getProvidedConfigScreenFactories());
        for (String modId : screens.keySet()) {
            map.put(modId, parent -> {
                ConfigScreen configScreen = screens.get(modId);
                configScreen.setParent(parent);
                WidgetDrawer.removeOffset(configScreen);
                return configScreen;
            });
        }
        return map;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenu::new;
    }

    static void addScreen(String modId, ConfigScreen screen) {
        screens.put(modId, screen);
    }
}