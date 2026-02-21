package me.kitty.radon.client;

import me.kitty.radon.Radon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Sound {

    public static final Identifier MENU_CLICK_ID = Identifier.of("radon", "menu_click");
    public static final Identifier MENU_SLIDE_ID = Identifier.of("radon", "menu_slide");
    public static final SoundEvent MENU_CLICK = SoundEvent.of(MENU_CLICK_ID);
    public static final SoundEvent MENU_SLIDE = SoundEvent.of(MENU_SLIDE_ID);

    public static void register() {

        Registry.register(Registries.SOUND_EVENT, MENU_CLICK_ID, MENU_CLICK);
        Registry.register(Registries.SOUND_EVENT, MENU_SLIDE_ID, MENU_SLIDE);

    }

    public static void play(SoundEvent soundEvent, float volume) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.
                //? if >1.21.10 {
                        /*ui
                *///? } else {
                ambient
                 //? }
                        (soundEvent, 1.0f, volume));
    }

    public static void play(SoundEvent soundEvent) {
        play(soundEvent, 5.0f * Radon.volume);
    }

}
