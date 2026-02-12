package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.Slider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static me.kitty.radon.client.Sound.*;

public class SettingsScreen extends Screen {

    MinecraftClient mc = MinecraftClient.getInstance();

    public SettingsScreen(String text) {

        super(Text.literal(text));

    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        Slider volumeSlider = new Slider(
                10,
                40,
                75,
                15,
                "Volume: " + Math.round(Radon.volume * 100) + "%",
                (slider) -> {
                    Radon.volume = (float) slider.getValue();
                    slider.text = "Volume: " + Math.round(Radon.volume * 100) + "%";

                    slider.drawContext.drawCenteredTextWithShadow(mc.textRenderer, Text.of(slider.text), slider.getX() + width / 2, slider.getY() + (height - 8) / 2, slider.textColor);
                },
                MENU_SLIDE,
                MENU_CLICK,
                Radon.volume
        );

        Button backButton = new Button(
                10,
                10,
                50,
                15,
                "Back",
                () -> mc.execute(() -> mc.setScreen(new ModMenu("Radon Menu"))),
                MENU_CLICK
        );

        this.addDrawableChild(backButton);

        this.addDrawableChild(volumeSlider);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, this.title, width / 2, height / 2, 0xFFFFFF);

    }

}
