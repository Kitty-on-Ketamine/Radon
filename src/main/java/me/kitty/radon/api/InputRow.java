package me.kitty.radon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.kitty.radon.Radon;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class InputRow extends Row {
    private String placeholder;
    private String value;
    private String lastValue;
    private Input input;
    private final List<Consumer<String>> consumers = new ArrayList<>();
    private final int limit;

    InputRow(Tab tab, Key key, String description, List<String> tooltip, String placeholder, int limit, ConfigScreen screen) {
        super(tab, key, description, tooltip, screen);
        this.placeholder = placeholder;
        this.limit = limit;
        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(key.getKey(), v);
            screen.getSaver().save(config);
            lastValue = v;
        });
        reData();
    }

    InputRow(Section section, Key key, String description, List<String> tooltip, String placeholder, int limit, ConfigScreen screen) {
        super(section, key, description, tooltip, screen);
        this.placeholder = placeholder;
        this.limit = limit;
        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(key.getKey(), v);
            screen.getSaver().save(config);
            lastValue = v;
        });
        reData();
    }

    /**
     * Get the placeholder of this row's input
     * @return The placeholder in {@link String}
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Set the placeholder of this row's input
     * @param placeholder The placeholder in {@link String}
     */
    public void setPlaceholder(String placeholder) {
        if (!initialized) return;
        this.placeholder = placeholder;
        input.setPlaceholder(Text.literal(placeholder).setStyle(Radon.fontStyle));
    }

    /**
     * Get the value of the input in this row
     * @return {@link String} which is the value
     */
    public String getValue() {
        return value;
    }

    private void setValue(String value, Boolean subscribe) {
        this.value = value;
        if (!initialized) return;
        input.setText(value);
        if (subscribe) {
            for (Consumer<String> consumer : consumers) {
                consumer.accept(this.value);
            }
        }
    }

    /**
     * Set the value of the button
     * @param value The {@link String} value of the input
     */
    public void setValue(String value) {
        setValue(value, true);
    }

    /**
     * Get the character limit of this row's input
     * @return The character limit in {@link Integer}
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Run code if the value of the input changes
     * @param consumer A {@link Consumer} of {@link String}
     */
    public void subscribe(Consumer<String> consumer) {
        consumers.add(consumer);
    }

    /**
     * If instant save is disabled for the user, this will save the value of this row
     */
    @Override
    public void save() {
        if (Radon.instantSave) return;
        if (Objects.equals(lastValue, value)) return;
        for (Consumer<String> consumer : consumers) {
            consumer.accept(this.value);
        }
    }

    @Override
    void reRender() {
        super.reRender();

        input = new Input(
                screen.width - 10 - 75,
                0,
                75,
                limit,
                placeholder,
                i -> {
                    this.value = i.getText();
                    if (Radon.instantSave) {
                        for (Consumer<String> consumer : consumers) {
                            consumer.accept(this.value);
                        }
                    }
                },
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        );

        ((IScreenMixin) screen).addDrawableChildPublic(input);
        TickUtil.runNextTick(this::reData);
    }

    protected void reData() {
        JsonObject cfg = screen.getSaver().load();
        if (cfg != null) {
            JsonElement val = cfg.get(key.getKey());
            if (val == null) {
                cfg.addProperty(key.getKey(), "");
            }
            String value = cfg.get(key.getKey()).getAsString();
            if (!value.isEmpty() && value.length() <= limit) {
                setValue(value, false);
            }
        }

        init();
    }

    @Override
    protected void init() {
        super.init();
        lastValue = value;
    }

    /**
     * Get the input of the row
     * @return {@link Input}
     */
    @Override
    public Widget getWidget() {
        return input;
    }
}
