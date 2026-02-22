package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.RectBox;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class Tab {
    private final String name;
    private final ConfigScreen screen;
    private RectBox tab;
    private TextWidget text;
    private boolean active = false;
    private final Consumer<Tab> consumer;
    Tab(String name, Consumer<Tab> consumer, ConfigScreen screen) {
        this.name = name;
        this.screen = screen;
        this.consumer = consumer;
    }

    void reRender() {
        tab = new RectBox(
                0,
                35,
                0,
                54,
                false,
                false,
                true,
                true,
                0x880000,
                0xffffffff,
                List.of(),
                true,
                box -> {
                    active = true;
                    consumer.accept(this);
                }
        );

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        text = new TextWidget(
                0,
                34 + textRenderer.fontHeight / 3 * 2,
                60,
                textRenderer.fontHeight,
                Text.literal(name).setStyle(Radon.fontStyle),
                textRenderer
        );

        ((IScreenMixin) screen).addDrawableChildPublic(tab);
        ((IScreenMixin) screen).addDrawableChildPublic(text);
    }

    public RectBox getBox() {
        return tab;
    }

    public TextWidget getText() {
        return text;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}
