package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RectBox implements Drawable, Element, Selectable {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public boolean side1;
    public boolean side2;
    public boolean side3;
    public boolean side4;

    private final int color;
    private final int outline;
    private final List<Text> tooltip;
    private Boolean hidden;
    public boolean visible;
    public boolean isStatic;
    public HashMap<RectBox, Long> now = new HashMap<>();

    private boolean on = false;
    private int fade = 0;

    public RectBox(int x1, int y1, int x2, int y2, boolean side1, boolean side2, boolean side3, boolean side4, int color, int outline, List<String> tooltip, boolean isStatic) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
        this.side4 = side4;
        this.color = color;
        this.outline = outline;
        this.isStatic = isStatic;

        this.hidden = false;
        this.visible = true;

        List<Text> tooltipList = new ArrayList<>();

        for (String entry : tooltip) tooltipList.add(Text.literal(entry).setStyle(Radon.fontStyle));

        this.tooltip = tooltipList;

        this.now.put(this, System.currentTimeMillis());

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (this.hidden) return;
        if (!this.visible) return;

        if (on && System.currentTimeMillis() - now.get(this) >= 5 && !isStatic) {

            fade = Math.min(fade + 1, 25);
            now.put(this, System.currentTimeMillis());

        }

        if (!on && System.currentTimeMillis() - now.get(this) >= 5 && !isStatic) {

            fade = Math.max(fade - 1, 0);
            now.put(this, System.currentTimeMillis());

        }

        if (isStatic) on = true;

        if (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2 || isStatic) {

            if (!on) {

                mc.getSoundManager().play(PositionedSoundInstance.
                        //? if >1.21.8 {
                                ui
                        //? } else {
                        /*ambient
                         *///? }
                                (Sound.MENU_SLIDE, 1.0f, 5.0f * Radon.volume));
                on = true;

            }

            context.fill(
                    x1 - 2,
                    y1 - 2,
                    x2 + 2,
                    y2 + 2,
                    color
            );

            if (tooltip != null && !tooltip.isEmpty()) {

                context.drawTooltip(
                        MinecraftClient.getInstance().textRenderer,
                        tooltip.stream().map(Text::of).toList(),
                        mouseX,
                        mouseY
                );

            }

        } else if (on) {

            on = false;

        }

        int alpha = Math.min(fade * 10, 255);

        int outlineColor = (alpha << 24) | (outline & 0x00FFFFFF);

        if (isStatic) outlineColor = outline;

        // TOP
        if (side1) {

            context.fill(
                    x1 - 3,
                    y1 - 3,
                    x2 + 3,
                    y1 - 2,
                    outlineColor
            );

            context.fill(
                    x1 - 2,
                    y1 - 4,
                    x2 + 4,
                    y1 - 3,
                    outlineColor
            );

        } else {

            context.fill(
                    x1 - 4,
                    y1 - 4,
                    x2 + 2,
                    y1 - 2,
                    outlineColor
            );

        }

        // BOTTOM
        if (side3) {

            context.fill(
                    x1 - 2,
                    y2 + 3,
                    x2 + 3,
                    y2 + 2,
                    outlineColor
            );

            context.fill(
                    x1 - 2,
                    y2 + 4,
                    x2 + 2,
                    y2 + 3,
                    outlineColor
            );

        } else {

            context.fill(
                    x1 - 2,
                    y2 + 2,
                    x2 + 4,
                    y2 + 4,
                    outlineColor
            );

        }

        // LEFT
        if (side4) {

            context.fill(
                    x1 - 3,
                    y1 - 2,
                    x1 - 2,
                    y2 + 3,
                    outlineColor
            );

            context.fill(
                    x1 - 4,
                    y1 - 2,
                    x1 - 3,
                    y2 + 2,
                    outlineColor
            );

        } else {

            context.fill(
                    x1 - 4,
                    y1 - 2,
                    x1 - 2,
                    y2 + 4,
                    outlineColor
            );

        }

        // RIGHT
        if (side2) {

            context.fill(
                    x2 + 3,
                    y1 - 3,
                    x2 + 2,
                    y2 + 2,
                    outlineColor
            );

            context.fill(
                    x2 + 4,
                    y1 - 2,
                    x2 + 3,
                    y2 + 2,
                    outlineColor
            );

        } else {

            context.fill(
                    x2 + 4,
                    y1 - 4,
                    x2 + 2,
                    y2 + 2,
                    outlineColor
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
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public void hide() {

        hidden = true;

    }

}