package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ThinBox implements Drawable, Element, Selectable {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    private boolean right;
    private boolean left;

    private final int color;
    private final int outline;
    private final int outlineActive;
    private final List<Text> tooltip;
    public boolean visible;
    public HashMap<ThinBox, Long> now = new HashMap<>();
    private final Consumer<ThinBox> onClick;

    public ThinBox(int x1, int y1, int x2, int y2, boolean right, boolean left, int color, int outline, int outlineActive, List<String> tooltip, Consumer<ThinBox> onClick) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.outline = outline;
        this.outlineActive = outlineActive;

        this.right = right;
        this.left = left;

        this.visible = true;

        List<Text> tooltipList = new ArrayList<>();

        for (String entry : tooltip) tooltipList.add(Text.literal(entry).setStyle(Radon.fontStyle));

        this.tooltip = tooltipList;

        this.now.put(this, System.currentTimeMillis());
        this.onClick = onClick;

    }

    //? >1.21.8 {
    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!visible) return false;

        if (click.x() >= x1 && click.x() <= x2 &&
                click.y() >= y1 && click.y() <= y2) {

            if (onClick != null) {
                onClick.accept(this);
            }

            return true;
        }

        return false;
    }
    //? } else {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible) return false;

        if (mouseX >= x1 && mouseX <= x2 &&
            mouseY >= y1 && mouseY <= y2) {

            if (onClick != null) {
                onClick.accept(this);
            }

            return true;
        }

        return false;
    }
    *///? }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (!this.visible) return;

        int outlineColor = mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2 ? outlineActive : outline;

        if (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2) {

            if (tooltip != null && !tooltip.isEmpty()) {

                context.drawTooltip(
                        MinecraftClient.getInstance().textRenderer,
                        tooltip.stream().map(Text::of).toList(),
                        mouseX,
                        mouseY
                );

            }

        }

        context.fill(
                x1 - 2,
                y1 - 2,
                x2 + 2,
                y2 + 2,
                color
        );

        // BOTTOM
        context.fill(
                x1 - 2,
                y2 + 2,
                x2 + 2,
                y2 + 3,
                outlineColor
        );

        // LEFT

        if (left || (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2)) {

            context.fill(
                    x1 - 2,
                    y1 - 2,
                    x1 - 3,
                    y2 + 3,
                    outlineColor
            );

        }

        // RIGHT
        context.fill(
                x2 + 2,
                y1 - 2,
                x2 + 3,
                y2 + 3,
                outlineColor
        );

    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public void setRight(boolean bool) {

        right = bool;

    }

    public void setLeft(boolean bool) {

        left = bool;

    }

}