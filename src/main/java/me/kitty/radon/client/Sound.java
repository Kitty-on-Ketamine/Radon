package me.kitty.radon.client;

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

}
