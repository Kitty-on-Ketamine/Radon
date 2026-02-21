package me.kitty.radon;

import me.kitty.radon.Screens.Settings;
import me.kitty.radon.client.Sound;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Style;
//? if >1.21.8 {
/*import net.minecraft.text.StyleSpriteSource;
*///? }
import net.minecraft.util.Identifier;

public class Radon implements ModInitializer {

    public static float volume = 0.5f;
    public static float scaleMultiplier = 0;
    public static String inputText = "";
    public static final Settings settings = (Settings) new Settings().initSaver();

    //? if > 1.21.8 {
    /*public static Style fontStyle = Style.EMPTY.withFont(new StyleSpriteSource.Font(Identifier.of("radon", "default")));
    *///? } else {
    public static Style fontStyle = Style.EMPTY.withFont(Identifier.of("radon", "default"));
     //? }
    public static boolean instantSave = true;

    @Override
    public void onInitialize() {

        Sound.register();

    }

}