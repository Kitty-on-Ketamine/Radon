package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.RectBox;
import me.kitty.radon.Widgets.ThinBox;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class Tab {
    private final String name;
    private final ConfigScreen screen;
    private ThinBox tab;
    private TextWidget text;
    private boolean active = false;
    private final Consumer<Tab> consumer;

    private static boolean right = false;
    private static boolean left = false;

    Tab(String name, Consumer<Tab> consumer, ConfigScreen screen) {
        this.name = name;
        this.screen = screen;
        this.consumer = consumer;
    }

    void reRender() {
        tab = new ThinBox(
                0,
                36,
                0,
                50,
                right,
                left,
                0x88000000,
                0xff888888,
                0xffffffff,
                List.of(),
                box -> {
                    active = true;
                    box.setActive(true);
                    consumer.accept(this);
                    Sound.play(Sound.MENU_CLICK);
                }
        );

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        text = new TextWidget(
                0,
                33 + textRenderer.fontHeight / 3 * 2,
                60,
                textRenderer.fontHeight,
                Text.literal(name).setStyle(Radon.fontStyle),
                textRenderer
        );

        ((IScreenMixin) screen).addDrawableChildPublic(tab);
        ((IScreenMixin) screen).addDrawableChildPublic(text);
    }

    public ThinBox getBox() {
        return tab;
    }

    public TextWidget getText() {
        return text;
    }

    public void setActive(boolean active) {

        this.active = active;
        if (tab != null) getBox().setActive(active);

    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return name;
    }

    public ConfigScreen getScreen() {
        return screen;
    }

    public void setRight(boolean bool) {

        right = bool;

    }

    public void setLeft(boolean bool) {

        left = bool;

    }

}
