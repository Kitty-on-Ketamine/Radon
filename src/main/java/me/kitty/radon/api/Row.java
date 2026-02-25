package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Box;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class Row {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    protected final String description;
    protected final List<String> tooltipTexts;
    protected boolean initialized = false;
    private Box box;
    private TextWidget label;
    protected final ConfigScreen screen;
    private final List<Runnable> runnables = new ArrayList<>();
    protected final Tab tab;
    protected final Key key;
    protected Section section;

    Row(Tab tab, Key key, String description, List<String> tooltip, ConfigScreen screen) {
        this.description = description;
        this.tooltipTexts = tooltip;
        this.screen = screen;
        this.tab = tab;
        this.key = key;
    }

    Row(Section section, Key key, String description, List<String> tooltip, ConfigScreen screen) {
        this.description = description;
        this.tooltipTexts = tooltip;
        this.screen = screen;
        this.tab = section.getTab();
        this.key = key;
        this.section = section;
    }

    void reRender() {
        TickUtil.runNextTick(this::init);

        box = new Box(
                10,
                0,
                screen.width - 10,
                16,
                0x33000000,
                0xffffffff,
                tooltipTexts
        );

        label = new TextWidget(
                10,
                1,
                mc.textRenderer.getWidth(description),
                16,
                Text.literal(description).setStyle(Radon.fontStyle),
                mc.textRenderer
        );

        ((IScreenMixin) screen).addDrawableChildPublic(box);
        ((IScreenMixin) screen).addDrawableChildPublic(label);
    }

    /**
     * Get the box of this row
     * @return {@link Box}
     */
    public Box getBox() {
        return box;
    }

    /**
     * Get the TextWidget of this row
     * @return {@link TextWidget}
     */
    public TextWidget getLabel() {
        return label;
    }

    public abstract Widget getWidget();
    public abstract void save();

    /**
     * Get the tab which includes this row
     * @return {@link Tab}
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * Get the section which includes this row
     * @return null if no section, {@link Section} if there is
     */
    public Section getSection() {
        return section;
    }

    /**
     * Get the description of this row
     * @return {@link String} description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the tooltip texts of this row
     * @return A {@link List} of {@link String}
     */
    public List<String> getTooltipTexts() {
        return tooltipTexts;
    }

    /**
     * Run code when the data of the row loaded
     * @param runnable A {@link Runnable}
     */
    public void onInit(Runnable runnable) {
        runnables.add(runnable);
    }

    protected void init() {
        if (!initialized) {
            initialized = true;
            TickUtil.runNextTick(() -> {
                for (Runnable runnable : runnables) {
                    runnable.run();
                }
            });
        }
    }
}
