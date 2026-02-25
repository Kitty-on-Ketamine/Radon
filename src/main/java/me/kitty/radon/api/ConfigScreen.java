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
    private final List<Row> rows = new ArrayList<>();
    private final List<Tab> tabs = new ArrayList<>();
    private final List<Row> found = new ArrayList<>();
    private final List<Row> activeRows = new ArrayList<>();
    private final List<Section> sections = new ArrayList<>();
    private int heightOffset = 75;
    private int widthOffset = 10;
    private int scrollOffset = 0;
    private int wScrollOffset = 0;
    private Saver saver = null;
    private Screen parent = null;

    public ConfigScreen() {
        super(Text.empty());
    }

    @Override
    protected void init() {
        CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);

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
                (button) -> {
                    mc.execute(() -> mc.setScreen(parent));
                    CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);
                },
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
        for (Section section : sections) {
            section.reRender();
        }
        for (Tab tab : tabs) {
            tab.reRender();
            tab.getBox().setLeft(false);
        }

        if (!tabs.isEmpty()) tabs.getFirst().getBox().setLeft(true);

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

    //? if >1.21.10 {
    @Override
    public void resize(int width, int height) {
        CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);
        super.resize(width, height);
    }
    //? } else {
    /*@Override
    public void resize(MinecraftClient client, int width, int height) {
        CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);
        super.resize(client, width, height);
    }
    *///? }

    /**
     * The title of this screen rendered in-game
     */
    public abstract String getScreenTitle();
    /**
     * This runs when the screen initialized.
     * You want to create your rows and tabs here.
     */
    protected abstract void radon();

    int getHeightOffset() {
        return heightOffset;
    }
    Saver getSaver() {
        return saver;
    }

    private String getModId() {
        if (!RadonClient.modContainers.containsKey(this)) {
            return "radon";
        }
        return RadonClient.modContainers.get(this).getMetadata().getId();
    }

    /**
     * <p>This method is for Radon itself. If you are using the api by the docs, you can ignore this.</p>
     * Initializes saver and runs radon();
     */
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
    /**
     * <p>This method is for Radon itself. If you are using the api by the docs, you can ignore this.</p>
     * Sets where the back button will put back
     */
    public ConfigScreen setParent(Screen parent) {
        this.parent = parent;
        return this;
    }
    /**
     * <p>This method is for Radon itself. If you are using the api by the docs, you can ignore this.</p>
     * Sets where the back button will put back
     */
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

    /**
     * Search between the active rows (the rows on the active tab)
     * @param keyword The search term
     */
    public void search(String keyword) {
        if (keyword.isEmpty()) {
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

    /**
     * @return A Map of String, Row where String is the description of the row, and Row is the row
     */
    public Map<String, Row> getRows() {
        Map<String, Row> map = new HashMap<>();
        for (Row row : rows) {
            map.put(row.getDescription(), row);
        }
        return map;
    }

    /**
     * Create a {@link ButtonRow}
     * @param tab The tab which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param option boolean or enum option
     * @return new {@link ButtonRow}
     */
    public ButtonRow buttonRow(Tab tab, Key key, String description, List<String> tooltipTexts, Boolean option) {
        if (key == null) return null;
        ButtonRow row = new ButtonRow(tab, key, description, tooltipTexts, option, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }
    /**
     * Create a {@link ButtonRow}
     * @param tab The tab which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param option boolean or enum option
     * @return new {@link ButtonRow}
     */
    public ButtonRow buttonRow(Tab tab, Key key, String description, List<String> tooltipTexts, Enum<?> option) {
        if (key == null) return null;
        ButtonRow row = new ButtonRow(tab, key, description, tooltipTexts, option, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }
    /**
     * Create a SliderRow
     * @param tab The tab which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param initialValue The initial value of the slider
     * @param min The minimum value of the slider
     * @param max The maximum value of the slider
     * @return new {@link SliderRow}
     */
    public SliderRow sliderRow(Tab tab, Key key, String description, List<String> tooltipTexts, int initialValue, int min, int max) {
        if (key == null) return null;
        SliderRow row = new SliderRow(tab, key, description, tooltipTexts, initialValue, min, max, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }
    /**
     * Create an {@link InputRow}
     * @param tab The tab which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param placeholder When the input is empty and not focused, this will be written in it
     * @param limit Character limit in the input
     * @return new {@link InputRow}
     */
    public InputRow inputRow(Tab tab, Key key, String description, List<String> tooltipTexts, String placeholder, int limit) {
        if (key == null) return null;
        InputRow row = new InputRow(tab, key, description, tooltipTexts, placeholder, limit, this);
        heightOffset += 25;
        rows.add(row);
        return row;
    }

    /**
     * Create a {@link ButtonRow}
     * @param section The section which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param option boolean or enum option
     * @return new {@link ButtonRow}
     */
    public ButtonRow buttonRow(Section section, Key key, String description, List<String> tooltipTexts, Boolean option) {
        if (key == null) return null;
        ButtonRow row = new ButtonRow(section, key, description, tooltipTexts, option, this);
        heightOffset += 25;
        rows.add(row);
        section.addRow(row);
        return row;
    }
    /**
     * Create a {@link ButtonRow}
     * @param section The section which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param option boolean or enum option
     * @return new {@link ButtonRow}
     */
    public ButtonRow buttonRow(Section section, Key key, String description, List<String> tooltipTexts, Enum<?> option) {
        if (key == null) return null;
        ButtonRow row = new ButtonRow(section, key, description, tooltipTexts, option, this);
        heightOffset += 25;
        rows.add(row);
        section.addRow(row);
        return row;
    }
    /**
     * Create a SliderRow
     * @param section The section which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param initialValue The initial value of the slider
     * @param min The minimum value of the slider
     * @param max The maximum value of the slider
     * @return new {@link SliderRow}
     */
    public SliderRow sliderRow(Section section, Key key, String description, List<String> tooltipTexts, int initialValue, int min, int max) {
        if (key == null) return null;
        SliderRow row = new SliderRow(section, key, description, tooltipTexts, initialValue, min, max, this);
        heightOffset += 25;
        rows.add(row);
        section.addRow(row);
        return row;
    }
    /**
     * Create an {@link InputRow}
     * @param section The section which will include this row
     * @param description The description of the row
     * @param tooltipTexts Tooltip texts shown on hover; each string represents one line
     * @param placeholder When the input is empty and not focused, this will be written in it
     * @param limit Character limit in the input
     * @return new {@link InputRow}
     */
    public InputRow inputRow(Section section, Key key, String description, List<String> tooltipTexts, String placeholder, int limit) {
        if (key == null) return null;
        InputRow row = new InputRow(section, key, description, tooltipTexts, placeholder, limit, this);
        heightOffset += 25;
        rows.add(row);
        section.addRow(row);
        return row;
    }

    /**
     * Create a {@link Tab}
     * @param title The title (name) of the tab
     * @return new {@link Tab}
     */
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

    public Section section(Tab tab, String title) {
        Section section = new Section(tab, title, this);
        sections.add(section);
        return section;
    }

    /**
     * Creates a key
     * @param key A unique string, which can be only used once
     * @return new {@link Key} or if key already exists then null
     */
    public Key key(String key) {
        return Key.of(this, key);
    }

    private void render(List<Row> rows) {
        heightOffset = 75 - scrollOffset;
        Map<Section, Boolean> drawn = new HashMap<>();
        for (Section section : sections) {
            drawn.put(section, false);
        }
        for (Row row : this.rows) {
            int y = heightOffset;
            if (y < 75 || y > height - 60 || !rows.contains(row)) {
                if (!rows.contains(row)) heightOffset -= 25;
                row.getBox().y1 = -100;
                row.getBox().y2 = -100;
                row.getLabel().setY(-100);
                row.getWidget().setY(-100);
            } else {
                Section section = row.getSection();
                if (section != null) {
                    if (!drawn.get(section)) {
                        drawn.put(section, true);
                        section.getBox().y1 = heightOffset;
                        section.getBox().y2 = heightOffset + 16;
                        section.getText().setY(heightOffset + 1);
                        heightOffset += 25;
                        y = heightOffset;
                    }
                }
                int x = row.getSection() != null ? 20 : 10;
                row.getBox().x1 = x;
                row.getLabel().setX(x);
                row.getBox().y1 = y;
                row.getBox().y2 = y + 16;
                row.getLabel().setY(y + 1);
                row.getWidget().setY((row.getWidget() instanceof Input) ? y + 4 : y);
            }
            heightOffset += 25;
        }
        for (Section section : drawn.keySet()) {
            if (!drawn.get(section)) {
                section.getBox().y1 = -100;
                section.getBox().y2 = -100;
                section.getText().setY(-100);
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
}