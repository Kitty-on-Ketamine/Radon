package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Box;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.List;

public abstract class Row {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private final String description;
    private final List<String> tooltipTexts;
    protected boolean initialized = false;
    private Box box;
    private TextWidget label;
    protected final ConfigScreen screen;
    protected int height;

    Row(String description, List<String> tooltip, ConfigScreen screen) {
        this.description = description;
        this.tooltipTexts = tooltip;
        this.screen = screen;
        height = screen.getHeightOffset();
    }

    void resetHeight() {
        height = screen.getHeightOffset();
    }

    void reRender() {
        TickUtil.runNextTick(() -> initialized = true);

        box = new Box(
                10,
                height,
                screen.width - 10,
                height + 16,
                0x33000000,
                0xffffffff,
                tooltipTexts
        );

        label = new TextWidget(
                10,
                height + 1,
                200,
                16,
                Text.literal(description).setStyle(Radon.fontStyle),
                mc.textRenderer
        );

        ((IScreenMixin) screen).addDrawableChildPublic(box);
        ((IScreenMixin) screen).addDrawableChildPublic(label);
    }

    public Box getBox() {
        return box;
    }

    public TextWidget getLabel() {
        return label;
    }

    public abstract Widget getWidget();

    public String getDescription() {
        return description;
    }
    public List<String> getTooltipTexts() {
        return tooltipTexts;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
