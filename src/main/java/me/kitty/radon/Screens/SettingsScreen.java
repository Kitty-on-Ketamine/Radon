package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.DataUtils;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.Widgets.Slider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2f;

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

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        Button backButton = new Button(
                10,
                10,
                50,
                16,
                "Back",
                (button) -> mc.execute(() -> mc.setScreen(new ModMenu("Radon Menu"))),
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
                    slider.text = Text.literal("Volume: " + Math.round(Radon.volume * 100) + "%").setStyle(Style.EMPTY.withFont(
                            //? if > 1.21.8 {
                            new StyleSpriteSource.Font(
                                    //? }
                                    Identifier.of("radon", "default")
                                    //? if > 1.21.8 {
                            )
                            //? }
                    ));;

                    slider.drawContext.drawCenteredTextWithShadow(mc.textRenderer, Text.of(slider.text), slider.getX() + width / 2, slider.getY() + (height - 8) / 2, slider.textColor);

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
                (input) -> {Radon.inputText = input.getText();},
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

        context.drawCenteredTextWithShadow(textRenderer, this.title, width / 2, 10, 0xFFFFFFFF);

    }

}
