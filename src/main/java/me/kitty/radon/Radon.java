package me.kitty.radon;

import me.kitty.radon.client.Sound;
import net.fabricmc.api.ModInitializer;

public class Radon implements ModInitializer {

    public static float volume = 0.5f;

    @Override
    public void onInitialize() {

        Sound.register();

    }

}
