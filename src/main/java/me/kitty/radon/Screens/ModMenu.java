package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.StaticBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

import static me.kitty.radon.client.Sound.MENU_CLICK;

public class ModMenu extends Screen {

    MinecraftClient mc = MinecraftClient.getInstance();

    public ModMenu() {

        super(Text.literal("Radon Menu").setStyle(Radon.fontStyle));

    }

    @Override
    protected void init() {

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));

        Button backButton = new Button(
                10,
                5,
                50,
                16,
                "Exit",
                List.of(),
                0,
                (button) -> {

                    mc.setScreen(null);

                },
                MENU_CLICK
        );

        Button settingsButton = new Button(
                this.width - 20 - mc.textRenderer.getWidth("Settings"),
                5,
                50,
                16,
                "Settings",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new SettingsScreen("Settings"))),
                MENU_CLICK
        );

        Button conceptButton = new Button(
                20,
                height - 16 - 10,
                50,
                16,
                "Concept",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new ConceptScreen("SomeMod Settings", this))),
                MENU_CLICK
        );

        this.addDrawableChild(backButton);
        this.addDrawableChild(settingsButton);
        this.addDrawableChild(conceptButton);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, this.title, width / 2, 15, 0xFFFFFFFF);

    }

}
