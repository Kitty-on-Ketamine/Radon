package me.kitty.radon.Widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;

import java.util.List;

public class StaticBox implements Drawable, Element, Selectable {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private final int color;
    private final int outline;
    private final List<String> tooltip;

    public StaticBox(int x1, int y1, int x2, int y2, int color, int outline, List<String> tooltip) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.outline = outline;
        this.tooltip = tooltip;

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        context.fill(
                x1 - 2,
                y1 - 2,
                x2 + 2,
                y2 + 2,
                color
        );

        // TOP
        context.fill(
                x1 - 4,
                y1 - 4,
                x2 + 4,
                y1 - 2,
                outline
        );

        // BOTTOM
        context.fill(
                x1 - 4,
                y2 + 4,
                x2 + 4,
                y2 + 2,
                outline
        );

        // LEFT
        context.fill(
                x1 - 4,
                y1 - 4,
                x1 - 2,
                y2 + 4,
                outline
        );

        // RIGHT
        context.fill(
                x2 + 4,
                y1 - 4,
                x2 + 2,
                y2 + 2,
                outline
        );

        if (tooltip != null && !tooltip.isEmpty()) {

            context.drawTooltip(
                    MinecraftClient.getInstance().textRenderer,
                    tooltip.stream().map(Text::of).toList(),
                    mouseX,
                    mouseY
            );

        }

    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }
}