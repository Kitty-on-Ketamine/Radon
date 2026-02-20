package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Screens.ModMenu;
import me.kitty.radon.Widgets.*;
import me.kitty.radon.client.RadonClient;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ConfigScreen extends Screen {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private final List<String> descriptions = new ArrayList<>();
    private int heightOffset = 55;
    private int scrollOffset = 0;
    private final List<Row> rows = new ArrayList<>();
    private final Saver saver;

    private Screen parent = null;

    public ConfigScreen() {
        super(Text.of(""));
        saver = new Saver(getModId());
        radon();
    }

    @Override
    protected void init() {
        addDrawableChild(new Button(
                width - 50 - 10,
                5,
                50,
                16,
                "Radon",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new ModMenu(this))),
                Sound.MENU_CLICK
        ));

        addDrawableChild(new Button(
                10,
                5,
                50,
                16,
                "Back",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(parent)),
                Sound.MENU_CLICK
        ));

        addDrawableChild(new TextWidget(
                (width - mc.textRenderer.getWidth(getScreenTitle())) / 2,
                10,
                mc.textRenderer.getWidth(getScreenTitle()),
                mc.textRenderer.fontHeight,
                Text.literal(getScreenTitle()).setStyle(Radon.fontStyle),
                mc.textRenderer
        ));

        addDrawableChild(new Input(
                10,
                height - 16 - 10,
                75,
                15,
                "Search...",
                input -> search(input.getText()),
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        ));

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));
        addDrawableChild(new StaticBox(-2, height + 4, width + 2, height - 40, 0x33000000,  0xffffffff, List.of()));
        for (Row row : rows) {
            row.reRender();
        }
    }

    public abstract String getScreenTitle();
    int getHeightOffset() {
        return heightOffset;
    }
    Saver getSaver() {
        return saver;
    }
    protected abstract void radon();
    public ConfigScreen setParent(Screen parent) {
        this.parent = parent;
        return this;
    }

    public void search(String keyword) {
        if (keyword.isBlank()) {
            render(rows);
            return;
        }
        List<Row> found = new ArrayList<>();
        for (Row row : rows) {
            String text = row.getLabel().getMessage().getString();
            if (text.toLowerCase().replaceAll(" ", "").contains(keyword.toLowerCase().replaceAll(" ", ""))) {
                found.add(row);
            }
        }
        render(found);
    }

    private String getModId() {
        if (RadonClient.modContainers.get(this) == null) {
            return "radon";
        }
        return RadonClient.modContainers.get(this).getMetadata().getId();
    }

    public Map<String, Row> getRows() {
        Map<String, Row> map = new HashMap<>();
        for (Row row : rows) {
            map.put(row.getLabel().getMessage().getString(), row);
        }
        return map;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int scrollSpeed = 10;
        scrollOffset -= (int) (verticalAmount * scrollSpeed);
        int maxScroll = Math.max(0, rows.size() * 20 - height - 100);
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        render(rows);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public ButtonRow buttonRow(String description, List<String> tooltipTexts, Object option) {
        if (descriptions.contains(description)) return null;
        descriptions.add(description);
        heightOffset += 20;
        ButtonRow row = new ButtonRow(description, tooltipTexts, option, this);
        rows.add(row);
        return row;
    }
    public SliderRow sliderRow(String description, List<String> tooltipTexts, int initialValue, int min, int max) {
        if (descriptions.contains(description)) return null;
        descriptions.add(description);
        heightOffset += 20;
        SliderRow row = new SliderRow(description, tooltipTexts, initialValue, min, max, this);
        rows.add(row);
        return row;
    }
    public InputRow inputRow(String description, List<String> tooltipTexts, String placeholder, int limit) {
        if (descriptions.contains(description)) return null;
        descriptions.add(description);
        heightOffset += 20;
        InputRow row = new InputRow(description, tooltipTexts, placeholder, limit, this);
        rows.add(row);
        return row;
    }

    private void render(List<Row> visibleRows) {
        int rowIndex = 0;
        int startY = 75 - scrollOffset;

        for (Row row : this.rows) {
            int y = startY + rowIndex * 20;

            if (!visibleRows.contains(row) || y < 55 || y > height - 60) {
                row.getBox().y1 = -100;
                row.getBox().y2 = -100;
                row.getLabel().setY(-100);
                row.getWidget().setY(-100);
            } else {
                row.getBox().y1 = y;
                row.getBox().y2 = y + 16;
                row.getLabel().setY(y + 1);
                row.getWidget().setY((row.getWidget() instanceof Input) ? y + 4 : y);
            }

            rowIndex++;
        }
    }
}
