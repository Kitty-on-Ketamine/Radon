package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.DataUtils;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.Widgets.Slider;
import me.kitty.radon.Widgets.StaticBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;

import java.util.List;

import static me.kitty.radon.Radon.scaleMultiplier;
import static me.kitty.radon.client.Sound.*;

public class SettingsScreen extends Screen {

    MinecraftClient mc = MinecraftClient.getInstance();

    String title;

    public SettingsScreen(String title) {

        super(Text.literal(title));

        this.title = title;

    }

    @Override
    protected void init() {

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));

        Button backButton = new Button(
                10,
                10,
                50,
                16,
                "Back",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new ModMenu())),
                MENU_CLICK
        );

        Slider volumeSlider = new Slider(
                10,
                50,
                75,
                16,
                "Volume: " + Math.round(Radon.volume * 100) + "%",
                (slider) -> {

                    Radon.volume = (float) slider.getValue();
                    slider.updateText("Volume: " + Math.round(Radon.volume * 100) + "%");

                },
                MENU_SLIDE,
                MENU_CLICK,
                Radon.volume
        );

//        Button guiScaleButton = new Button(
//                10,
//                50,
//                75,
//                15,
//                "Gui Scale: " + Radon.scale,
//                (button) -> {
//
//                    Radon.scale = DataUtils.next(Radon.scale);
//                    button.setText("Gui Scale: " + Radon.scale);
//
//                },
//                MENU_CLICK
//        );

        Input testInput = new Input(
                10,
                70,
                75,
                256,
                Radon.inputText,
                (input) -> Radon.inputText = input.getText(),
                MENU_CLICK,
                MENU_CLICK,
                MENU_SLIDE
        );

        this.addDrawableChild(backButton);
        this.addDrawableChild(volumeSlider);
        this.addDrawableChild(testInput);
//        this.addDrawableChild(guiScaleButton);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, Text.literal(this.title).setStyle(Radon.fontStyle), width / 2, 15, 0xFFFFFFFF);

    }

}
