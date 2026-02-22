package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.client.Draw;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class StaticBox implements Drawable, Element, Selectable {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private int lastWidth = -1;
    private int lastHeight = -1;

    private final int color;
    private final int outline;
    private final List<String> tooltip;
    private static final long seed = System.nanoTime();

    public record Icons(int frequency1, Identifier icon1, int frequency2, Identifier icon2) {}
    private Icons icon;

    private boolean initialized = false;

    private HashMap<Integer, ArrayList<Identifier>> textures = new HashMap<>();

    public StaticBox(int x1, int y1, int x2, int y2, int color, int outline, List<String> tooltip, Icons icons) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.outline = outline;
        this.tooltip = tooltip;
        this.icon = icons;

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (!initialized) {
            initTextures();
        }

        if (needsResize()) {
            initTextures();
        }

        if (!Radon.defaultBackground) {

            int cols = Math.min(textures.get(0).size(), (x2 - x1 + 31) / 32);
            int rows = Math.min(textures.size(), (y2 - y1 + 31) / 32);

            for (int row = 0; row < rows; row++) {

                ArrayList<Identifier> list = textures.get(row);
                if (list == null) continue;

                for (int col = 0; col < cols; col++) {

                    if (col >= list.size()) continue;

                    Draw.draw(
                            context,
                            list.get(col),
                            x1 + col * 32,
                            y1 + row * 32,
                            0, 0,
                            32, 32,
                            32, 32
                    );

                }

            }

        }

        context.fill(x1, y1, x2, y2, color);

        int b = 2;

        // Shadow

        // Bottom
        context.fill(x1 - b + 2, y2 + 2, x2 + b + 2, y2 + b + 2, 0x55000000);

        // Bottom
        context.fill(x1 - b + 2, y2 + 2, x2 + b + 2, y2 + b + 2, 0x55000000);

        // Left
        context.fill(x1 - b + 2, y1 + 2, x1 + 2, y2 + 2, 0x55000000);

        // Right
        context.fill(x2 + 2, y1 + 2, x2 + b + 2, y2 + 2, 0x55000000);



        // Outline

        // Top
        context.fill(x1 - b, y1 - b, x2 + b, y1, outline);

        // Bottom
        context.fill(x1 - b, y2, x2 + b, y2 + b, outline);

        // Left
        context.fill(x1 - b, y1, x1, y2, outline);

        // Right
        context.fill(x2, y1, x2 + b, y2, outline);

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

    private void initTextures() {

        Random r = new Random();

        int width  = x2 - x1;
        int height = y2 - y1;

        int neededCols = (width + 31) / 32;
        int neededRows = (height + 31) / 32;

        for (int row = 0; row < neededRows; row++) {

            ArrayList<Identifier> list = textures.getOrDefault(row, new ArrayList<>());

            if (list.isEmpty()) {

                list = new ArrayList<>();

                for (int col = 0; col < neededCols; col++) {

                    list.add(randomIcon(row, col));

                }

                textures.put(row, list);

            } else {

                for (int col = list.size(); col < neededCols; col++) {

                    list.add(randomIcon(row, col));

                }

            }

        }

        lastWidth = width;
        lastHeight = height;
        initialized = true;

    }

    private Identifier randomIcon(int row, int col) {

        Random r = new Random(seed + (long) row * 10000 + col);
        int total = icon.frequency1 + icon.frequency2;
        return r.nextInt(total) < icon.frequency1 ? icon.icon1 : icon.icon2;

    }

    private boolean needsResize() {

        int w = x2 - x1;
        int h = y2 - y1;

        if (w != lastWidth || h != lastHeight) {
            lastWidth = w;
            lastHeight = h;
            return true;
        }

        return false;

    }

}