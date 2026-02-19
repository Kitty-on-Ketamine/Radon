package me.kitty.radon.api;

import me.kitty.radon.Radon;
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
    protected final long timestamp;
    private final Box box;
    private final TextWidget label;

    Row(String description, List<String> tooltip, ConfigScreen screen) {
        this.description = description;
        this.tooltipTexts = tooltip;
        this.timestamp = System.currentTimeMillis();

        int height = screen.getHeightOffset();

        box = new Box(
                10,
                height,
                screen.width - 10,
                height + 16,
                0x33000000,
                0xffffffff,
                tooltip
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
}
