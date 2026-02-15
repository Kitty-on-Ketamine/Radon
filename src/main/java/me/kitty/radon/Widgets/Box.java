package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Box implements Drawable, Element, Selectable {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    private final int color;
    private final int outline;
    private final List<Text> tooltip;
    private Boolean hidden;
    public boolean visible;

    private boolean on = false;

    public Box(int x1, int y1, int x2, int y2, int color, int outline, List<String> tooltip) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.outline = outline;

        this.hidden = false;
        this.visible = true;

        List<Text> tooltipList = new ArrayList<>();

        for (String entry : tooltip) tooltipList.add(Text.literal(entry).setStyle(Radon.fontStyle));

        this.tooltip = tooltipList;

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (this.hidden) return;
        if (!this.visible) return;

        if (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2) {

            if (!on) {

                mc.getSoundManager().play(PositionedSoundInstance.ui(Sound.MENU_SLIDE, 1.0f, 5.0f * Radon.volume));
                on = true;

            }

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
                    y1 - 3,
                    x2 + 4,
                    y1 - 2,
                    outline
            );

            context.fill(
                    x1 - 3,
                    y1 - 4,
                    x2 + 3,
                    y1 - 2,
                    outline
            );

            // BOTTOM
            context.fill(
                    x1 - 4,
                    y2 + 3,
                    x2 + 4,
                    y2 + 2,
                    outline
            );

            context.fill(
                    x1 - 3,
                    y2 + 4,
                    x2 + 3,
                    y2 + 2,
                    outline
            );

            // LEFT
            context.fill(
                    x1 - 4,
                    y1 - 2,
                    x1 - 2,
                    y2 + 2,
                    outline
            );

            // RIGHT
            context.fill(
                    x2 + 4,
                    y1 - 2,
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

        } else if (on) {

            on = false;

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