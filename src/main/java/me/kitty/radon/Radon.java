package me.kitty.radon;

import me.kitty.radon.client.Sound;
import net.fabricmc.api.ModInitializer;

public class Radon implements ModInitializer {

    @Override
    public void onInitialize() {

        Sound.register();

    }

}
