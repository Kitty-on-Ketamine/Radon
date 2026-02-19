package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Screens.ModMenu;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.Widgets.Slider;
import me.kitty.radon.Widgets.StaticBox;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigScreen extends Screen {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private int heightOffset = 75;
    private int scrollOffset = 0;
    private List<Row> rows = new ArrayList<>();

    private Screen parent = null;

    public ConfigScreen() {
        super(Text.of(""));
    }

    @Override
    protected void init() {
        heightOffset = 75;
        rows = new ArrayList<>();
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

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));
        addDrawableChild(new StaticBox(-2, height + 4, width + 2, height - 40, 0x33000000,  0xffffffff, List.of()));
        radon();
    }

    public abstract String getScreenTitle();
    int getHeightOffset() {
        return heightOffset;
    }
    protected abstract void radon();
    public void setParent(Screen parent) {
        this.parent = parent;
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
        heightOffset += 20;
        ButtonRow row = new ButtonRow(description, tooltipTexts, option, this);
        rows.add(row);
        render(rows);
        return row;
    }
    public SliderRow sliderRow(String description, List<String> tooltipTexts, int initialValue, int min, int max) {
        heightOffset += 20;
        SliderRow row = new SliderRow(description, tooltipTexts, initialValue, min, max, this);
        rows.add(row);
        render(rows);
        return row;
    }
    public InputRow inputRow(String description, List<String> tooltipTexts, String placeholder) {
        heightOffset += 20;
        InputRow row = new InputRow(description, tooltipTexts, placeholder, this);
        rows.add(row);
        render(rows);
        return row;
    }

    private void render(List<Row> rows) {
        heightOffset = 75 - scrollOffset;
        for (Row row : rows) {
            int y = heightOffset;
            heightOffset += 20;
            if (y < 75 || y > height - 60 || !this.rows.contains(row)) {
                if (!this.rows.contains(row)) heightOffset -= 20;
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
        }
    }
}
