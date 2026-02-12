package me.kitty.radon.Screens;

import me.kitty.radon.Widgets.Button;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static me.kitty.radon.client.Sound.MENU_CLICK;

public class ModMenu extends Screen {

    MinecraftClient mc = MinecraftClient.getInstance();

    String title;

    public ModMenu(String title) {

        super(Text.literal(title));

    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        Button backButton = new Button(
                10,
                10,
                50,
                15,
                "Back",
                () -> {

                    mc.setScreen(null);

                },
                MENU_CLICK
        );

        Button settingsButton = new Button(
                this.width - 20 - mc.textRenderer.getWidth("Settings"),
                10,
                50,
                15,
                "Settings",
                () -> mc.execute(() -> mc.setScreen(new SettingsScreen("Settings"))),
                MENU_CLICK
        );

        this.addDrawableChild(backButton);
        this.addDrawableChild(settingsButton);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, this.title, width / 2, height - 10, 0xFFFFFFFF);

    }

}
