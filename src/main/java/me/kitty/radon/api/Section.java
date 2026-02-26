package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.Box;
import me.kitty.radon.Widgets.StaticBox;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.List;

public class Section {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private final Tab tab;
    private String title;
    private final ConfigScreen screen;
    private StaticBox box;
    private TextWidget text;

    Section (Tab tab, String title, ConfigScreen screen) {
        this.tab = tab;
        this.title = title;
        this.screen = screen;
    }

    /**
     * Get the Tab of this section
     * @return {@link Tab}
     */
    public Tab getTab(){
        return tab;
    }

    /**
     * Get the title of this section
     * @return {@link String}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of this section
     */
    public void setTitle(String title) {
        this.title = title;
    }

    void reRender() {
        box = new StaticBox(
                10,
                0,
                screen.width - 10,
                16,
                0x33000000,
                0xffffffff,
                List.of(),
                null
        );

        text = new TextWidget(
                screen.width / 2,
                1,
                mc.textRenderer.getWidth(title),
                16,
                Text.literal(title).setStyle(Radon.fontStyle),
                mc.textRenderer
        );

        ((IScreenMixin) screen).addDrawableChildPublic(box);
        ((IScreenMixin) screen).addDrawableChildPublic(text);
    }

    /**
     * Get the box of this section
     * @return {@link Box}
     */
    public StaticBox getBox() {
        return box;
    }

    /**
     * Get the TextWidget of this section
     * @return {@link TextWidget}
     */
    public TextWidget getText() {
        return text;
    }
}
