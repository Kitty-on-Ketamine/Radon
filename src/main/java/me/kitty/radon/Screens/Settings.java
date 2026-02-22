package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.Widgets.Slider;
import me.kitty.radon.api.ButtonRow;
import me.kitty.radon.api.ConfigScreen;
import me.kitty.radon.api.SliderRow;
import me.kitty.radon.api.Tab;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
//? if >1.21.8 {
import net.minecraft.text.StyleSpriteSource;
//? }
import net.minecraft.util.Identifier;

import java.util.List;

public class Settings extends ConfigScreen {
    @Override
    public String getScreenTitle() {
        return "Radon Settings";
    }

    @Override
    protected void radon() {
        Tab audioTab = tab("Audio");
        Tab textureTab = tab("Texture");
        Tab miscTab = tab("Misc");
        SliderRow volume = sliderRow(audioTab, "Volume", List.of("Slide to set the volume"), 50, 0, 100);
        volume.subscribe(vol -> Radon.volume = (float) vol / 100);
        volume.onInit(() -> Radon.volume = (float) volume.getValue() / 100);

        ButtonRow font = buttonRow(textureTab, "Default font", List.of("Should the mod use", "it's custom font or", "use the default", "Minecraft font"), false);
        font.subscribe(f -> {
            //? if >1.21.8 {
            Radon.fontStyle = Style.EMPTY.withFont(new StyleSpriteSource.Font(Identifier.of((boolean) f ? "minecraft" : "radon", "default")));
            //? } else {
            /*Radon.fontStyle = Style.EMPTY.withFont(Identifier.of((boolean) f ? "minecraft" : "radon", "default"));
             *///? }
            MinecraftClient.getInstance().reloadResources();
        });
        font.onInit(() -> {
            //? if >1.21.8 {
            Radon.fontStyle = Style.EMPTY.withFont(new StyleSpriteSource.Font(Identifier.of((boolean) font.getValue() ? "minecraft" : "radon", "default")));
            //? } else {
            /*Radon.fontStyle = Style.EMPTY.withFont(Identifier.of((boolean) font.getValue() ? "minecraft" : "radon", "default"));
             *///? }
        });

        /*ButtonRow textures = buttonRow(textureTab, "Default textures", List.of("Should the mod use", "it's custom textures or", "use the default", "Minecraft textures"), false);
        textures.subscribe(t -> {
            boolean value = (boolean) t;
            changeTextures(value);
            MinecraftClient.getInstance().reloadResources();
        });
        textures.onInit(() -> {
            boolean value = (boolean) textures.getValue();
            changeTextures(value);
        });*/

        ButtonRow background = buttonRow(textureTab, "Default background", List.of("Should the mod use", "it's custom background", "ore use the default"), false);
        background.subscribe(b -> {
            Radon.defaultBackground = (boolean) b;
            MinecraftClient.getInstance().reloadResources();
        });
        background.onInit(() -> Radon.defaultBackground = (boolean) background.getValue());

        ButtonRow instantSave = buttonRow(miscTab,"Instant save", List.of("Save the option", "you edited instantly", "or only when you", "press the save button"), true);
        instantSave.subscribe(i -> {
            Radon.instantSave = (boolean) i;
            MinecraftClient.getInstance().reloadResources();
        });
        instantSave.onInit(() -> Radon.instantSave = (boolean) instantSave.getValue());
    }

    private void changeTextures(boolean value) {
        Button.TEXTURE_DISABLED = Identifier.of(value ? "minecraft" : "radon", Button.TEXTURE_DISABLED.getPath());
        Button.TEXTURE_HOVER = Identifier.of(value ? "minecraft" : "radon", Button.TEXTURE_HOVER.getPath());
        Button.TEXTURE_NORMAL = Identifier.of(value ? "minecraft" : "radon", Button.TEXTURE_NORMAL.getPath());
        Input.TEXTURE_DISABLED = Identifier.of(value ? "minecraft" : "radon", Input.TEXTURE_DISABLED.getPath());
        Input.TEXTURE_HOVER = Identifier.of(value ? "minecraft" : "radon", Input.TEXTURE_HOVER.getPath());
        Input.TEXTURE_NORMAL = Identifier.of(value ? "minecraft" : "radon", Input.TEXTURE_NORMAL.getPath());
        Slider.TEXTURE_NORMAL = Identifier.of(value ? "minecraft" : "radon", Slider.TEXTURE_NORMAL.getPath());
        Slider.TEXTURE_HOVER = Identifier.of(value ? "minecraft" : "radon", Slider.TEXTURE_HOVER.getPath());
        Slider.TEXTURE_HANDLE = Identifier.of(value ? "minecraft" : "radon", Slider.TEXTURE_HANDLE.getPath());
        Slider.TEXTURE_HANDLE_HOVER = Identifier.of(value ? "minecraft" : "radon", Slider.TEXTURE_HANDLE_HOVER.getPath());
    }
}
