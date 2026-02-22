package me.kitty.radon.client;

import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.api.ConfigScreen;
import me.kitty.radon.Screens.ModMenu;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;
import java.util.Map;

public class RadonClient implements ClientModInitializer {

    public static Map<ConfigScreen, ModContainer> modContainers = new HashMap<>();

    @Override
    public void onInitializeClient() {

        KeyBinding OPEN_MENU = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.radon.open_menu",
                        InputUtil.Type.KEYSYM,
                        InputUtil.GLFW_KEY_RIGHT_SHIFT,
                        //? >1.21.8 {
                        KeyBinding.Category.MISC
                        //? } else {
                        /*"key.categories.misc"
                        *///? }
                )
        );


        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (OPEN_MENU.wasPressed()) {

                MinecraftClient.getInstance().setScreen(new ModMenu());

            }
        });

        for (EntrypointContainer<ConfigScreen> container : FabricLoader.getInstance().getEntrypointContainers("radon", ConfigScreen.class)) {
            ConfigScreen screen = container.getEntrypoint();
            ModContainer mod = container.getProvider();
            modContainers.put(screen, mod);
            screen.initSaver();
            ModMenuIntegration.addScreen(mod.getMetadata().getId(), screen);

        }

        TickUtil.init();

    }

}
