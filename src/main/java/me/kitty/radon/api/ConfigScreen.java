package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Screens.ModMenu;
import me.kitty.radon.Utils.CursorHelper;
import me.kitty.radon.Widgets.*;
import me.kitty.radon.client.RadonClient;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
//? if >1.21.8 {
import net.minecraft.client.gui.Click;
//? }
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.kitty.radon.Radon.*;

public abstract class ConfigScreen extends Screen {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private final List<String> descriptions = new ArrayList<>();
    private int heightOffset = 75;
    private int widthOffset = 10;
    private int scrollOffset = 0;
    private int wScrollOffset = 0;
    private final List<Row> rows = new ArrayList<>();
    private Saver saver = null;
    private final List<Tab> tabs = new ArrayList<>();
    private final List<Row> found = new ArrayList<>();
    private final List<Row> activeRows = new ArrayList<>();

    private Screen parent = null;

    public ConfigScreen() {
        super(Text.empty());
    }

    @Override
    protected void init() {

        if (!defaultBackground) {
            addDrawableChild(new StaticBox(-2, -2, width + 2, height + 2, 0x33000000, 0xffffffff, List.of(), new StaticBox.Icons(95, bg, 5, coal)));
        }

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of(), new StaticBox.Icons(100, static_bg, 0, static_bg)));
        addDrawableChild(new StaticBox(-2, height - 30, width + 2, height + 2, 0x33000000,  0xffffffff, List.of(), new StaticBox.Icons(100, static_bg, 0, static_bg)));

        addDrawableChild(new Button(
                width - 50 - 10,
                5,
                50,
                16,
                "Radon",
                List.of(),
                Radon.defaultTextures ? 0xffffffff : 0,
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
                Radon.defaultTextures ? 0xffffffff : 0,
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
                height - 16 - 7,
                75,
                15,
                "Search...",
                input -> search(input.getText()),
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        ));

        if (!Radon.instantSave) {
            addDrawableChild(new Button(
                    width - 10 - 50,
                    height - 16 - 7,
                    50,
                    16,
                    "Save",
                    List.of(),
                    0,
                    button -> {
                        for (Row row : rows) {
                            row.save();
                        }
                    },
                    Sound.MENU_CLICK
            ));
        }

        for (Row row : rows) {
            row.reRender();
        }
        for (Tab tab : tabs) {
            tab.reRender();
            tab.getBox().setLeft(false);
        }

        tabs.getFirst().getBox().setLeft(true);

        render(activeRows);
        renderTabs(tabs);

    }

    //? if >1.21.8 {
    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (Tab tab : tabs) {
            if (tab.getBox().mouseClicked(click, doubled)) {
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }
    //? } else {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Tab tab : tabs) {
            if (tab.getBox().mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    *///? }


    public abstract String getScreenTitle();
    int getHeightOffset() {
        return heightOffset;
    }
    Saver getSaver() {
        return saver;
    }
    public ConfigScreen initSaver() {
        if (saver == null) {
            saver = new Saver(getModId());
            radon();
            for (Tab tab : tabs) {
                if (tab.isActive()) {
                    activeRows.clear();
                    for (Row row : rows) {
                        if (row.getTab() == tab) {
                            activeRows.add(row);
                        }
                    }
                }
            }
        }
        return this;
    }
    protected abstract void radon();
    public ConfigScreen setParent(Screen parent) {
        this.parent = parent;
        return this;
    }
    public ConfigScreen fromTop() {
        scrollOffset = 0;
        wScrollOffset = 0;
        for (Tab tab : tabs) {
            tab.setActive(false);
        }
        Tab first = tabs.getFirst();
        first.setActive(true);
        activeRows.clear();
        for (Row row : rows) {
            if (row.getTab() == first) {
                activeRows.add(row);
            }
        }
        return this;
    }

    public void search(String keyword) {
        if (keyword.isBlank()) {
            render(activeRows);
            return;
        }
        found.clear();
        for (Row row : activeRows) {
            String text = row.getLabel().getMessage().getString();
            if (text.toLowerCase().replaceAll(" ", "").contains(keyword.toLowerCase().replaceAll(" ", ""))) {
                found.add(row);
            }
        }
        render(found);
    }

    private String getModId() {
        if (!RadonClient.modContainers.containsKey(this)) {
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
        if (mouseY >= 75) {
            int scrollSpeed = 10;
            scrollOffset -= (int) (verticalAmount * scrollSpeed);
            int maxScroll = Math.max(0, activeRows.size() * 25 - (height - 120));
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxScroll) scrollOffset = maxScroll;
            render(found.isEmpty() ? activeRows : found);
        } else {
            int scrollSpeed = 30;
            wScrollOffset -= (int) (verticalAmount * scrollSpeed);
            int maxScroll = Math.max(0, tabs.size() * 65 - (width - 16));
            if (wScrollOffset < 0) wScrollOffset = 0;
            if (wScrollOffset > maxScroll) wScrollOffset = maxScroll;
            renderTabs(tabs);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public ButtonRow buttonRow(Tab tab, String description, List<String> tooltipTexts, Object option) {
        if (descriptions.contains(description)) return null;
        descriptions.add(description);
        ButtonRow row = new ButtonRow(tab, description, tooltipTexts, option, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }
    public SliderRow sliderRow(Tab tab, String description, List<String> tooltipTexts, int initialValue, int min, int max) {
        if (descriptions.contains(description)) return null;
        descriptions.add(description);
        SliderRow row = new SliderRow(tab, description, tooltipTexts, initialValue, min, max, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }
    public InputRow inputRow(Tab tab, String description, List<String> tooltipTexts, String placeholder, int limit) {
        if (descriptions.contains(description)) return null;
        descriptions.add(description);
        InputRow row = new InputRow(tab, description, tooltipTexts, placeholder, limit, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }
    public Tab tab(String title) {
        Tab tab = new Tab(title, t -> {
            scrollOffset = 0;
            for (Tab tabs : tabs) {
                tabs.setActive(tabs == t);
            }
            activeRows.clear();
            for (Row row : rows) {
                if (row.getTab() == t) {
                    activeRows.add(row);
                }
            }
            render(activeRows);
        }, this);
        tabs.add(tab);
        if (tabs.size() == 1) tab.setActive(true);
        return tab;
    }

    private void render(List<Row> rows) {
        heightOffset = 75 - scrollOffset;
        for (Row row : this.rows) {
            int y = heightOffset;
            heightOffset += 25;
            if (y < 75 || y > height - 60 || !rows.contains(row)) {
                if (!rows.contains(row)) heightOffset -= 25;
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
    private void renderTabs(List<Tab> tabs) {
        widthOffset = 10 - wScrollOffset;
        for (Tab tab : this.tabs) {
            int x = widthOffset;
            widthOffset += 65;
            if (x + (tab.getBox().x2 - tab.getBox().x1) < 10 || x > width - 10 || !tabs.contains(tab)) {
                if (!tabs.contains(tab)) widthOffset -= 70;
                tab.getBox().x1 = -100;
                tab.getBox().x2 = -40;
                tab.getText().setX(-100);
            } else {
                tab.getBox().x1 = x;
                tab.getBox().x2 = x + 60;
                tab.getText().setX(x
                        //? if >1.21.8 {
                        + 30 - MinecraftClient.getInstance().textRenderer.getWidth(tab.getName()) / 2
                        //? }
                );
            }
        }
    }

    @Override
    public void resize(int width, int height) {

        CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);

        super.resize(width, height);

    }

}
