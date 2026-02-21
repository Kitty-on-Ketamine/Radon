package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.api.ButtonRow;
import me.kitty.radon.api.ConfigScreen;
import me.kitty.radon.api.SliderRow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
//? if >1.21.8 {
/*import net.minecraft.text.StyleSpriteSource;
*///? }
import net.minecraft.util.Identifier;

import java.util.List;

public class Settings extends ConfigScreen {
    @Override
    public String getScreenTitle() {
        return "Radon Settings";
    }

    @Override
    protected void radon() {
        SliderRow volume = sliderRow("Volume", List.of("Slide to set the volume"), 50, 0, 100);
        volume.subscribe(vol -> Radon.volume = (float) vol / 100);
        ButtonRow font = buttonRow("Default font", List.of("Should the mod use", "its custom font or", "use the default", "Minecraft font"), false);
        font.subscribe(f -> {
            //? if >1.21.8 {
            /*Radon.fontStyle = Style.EMPTY.withFont(new StyleSpriteSource.Font(Identifier.of((boolean) f ? "minecraft" : "radon", "default")));
            *///? } else {
            Radon.fontStyle = Style.EMPTY.withFont(Identifier.of((boolean) f ? "minecraft" : "radon", "default"));
             //? }
            MinecraftClient.getInstance().reloadResources();
        });
        ButtonRow instantSave = buttonRow("Instant save", List.of("Save the option", "you edited instantly", "or only when you", "press the save button"), true);
        instantSave.subscribe(i -> {
            Radon.instantSave = (boolean) i;
            MinecraftClient.getInstance().reloadResources();
        });
        volume.onInit(() -> Radon.volume = (float) volume.getValue() / 100);
        font.onInit(() -> {
            //? if >1.21.8 {
            /*Radon.fontStyle = Style.EMPTY.withFont(new StyleSpriteSource.Font(Identifier.of((boolean) font.getValue() ? "minecraft" : "radon", "default")));
            *///? } else {
            Radon.fontStyle = Style.EMPTY.withFont(Identifier.of((boolean) font.getValue() ? "minecraft" : "radon", "default"));
             //? }
        });
        instantSave.onInit(() -> Radon.instantSave = (boolean) instantSave.getValue());
    }
}
