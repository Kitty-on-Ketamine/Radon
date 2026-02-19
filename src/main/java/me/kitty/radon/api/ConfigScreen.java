package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Screens.ModMenu;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.StaticBox;
import me.kitty.radon.Widgets.WidgetDrawer;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.List;

public abstract class ConfigScreen extends Screen {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    private Screen parent = null;

    public ConfigScreen() {
        super(Text.of(""));
    }

    @Override
    public void resize(int width, int height) {
        WidgetDrawer.removeOffset(this);
        super.resize(width, height);
    }

    @Override
    protected void init() {
        addDrawableChild(new Button(
                width - 50 - 10,
                5,
                50,
                16,
                "Radon",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new ModMenu(this))),
                Sound.MENU_CLICK
        ));

        addDrawableChild(new Button(
                10,
                5,
                50,
                16,
                "Back",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(parent)),
                Sound.MENU_CLICK
        ));

        addDrawableChild(new TextWidget(
                (width - mc.textRenderer.getWidth(getScreenTitle())) / 2,
                10,
                mc.textRenderer.getWidth(getScreenTitle()),
                mc.textRenderer.fontHeight,
                Text.literal(getScreenTitle()).setStyle(Radon.fontStyle),
                mc.textRenderer
        ));

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));
        addDrawableChild(new StaticBox(-2, height + 4, width + 2, height - 40, 0x33000000,  0xffffffff, List.of()));
        radon();
    }

    public abstract String getScreenTitle();
    protected abstract void radon();
    public void setParent(Screen parent) {
        this.parent = parent;
    }
    public ButtonRow buttonRow(String description, List<String> tooltipTexts, Object option) {
        return new ButtonRow(description, tooltipTexts, option, this);
    }
    public SliderRow sliderRow(String description, List<String> tooltipTexts, int initialValue, int min, int max) {
        return new SliderRow(description, tooltipTexts, initialValue, min, max, this);
    }
}
