package me.kitty.radon.client;

import me.kitty.radon.Screens.ModMenu;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class RadonClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        KeyBinding OPEN_MENU = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.radon.open_menu",
                        InputUtil.Type.KEYSYM,
                        InputUtil.GLFW_KEY_RIGHT_SHIFT,
                        //? >1.21.8 {
                        KeyBinding.Category.DEBUG
                        //? } else {
                        /*"DEBUG"
                        *///? }
                )
        );


        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (OPEN_MENU.wasPressed()) {

                MinecraftClient.getInstance().setScreen(new ModMenu());

            }

        });

    }

}
