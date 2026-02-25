package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.StaticBox;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private final Tab tab;
    private String title;
    private final List<Row> rows = new ArrayList<>();
    private final ConfigScreen screen;
    private StaticBox box;
    private TextWidget text;

    Section (Tab tab, String title, ConfigScreen screen) {
        this.tab = tab;
        this.title = title;
        this.screen = screen;
    }

    public Tab getTab() {
        return tab;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<Row> getRows() {
        return rows;
    }
    public void addRow(Row row) {
        rows.add(row);
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

    public StaticBox getBox() {
        return box;
    }

    public TextWidget getText() {
        return text;
    }
}
