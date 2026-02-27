package me.kitty.radon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.kitty.radon.Radon;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Slider;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SliderRow extends Row {
    private final int initialValue;
    private final int min;
    private final int max;
    private long value;
    private long lastValue;
    private Slider slider;
    private final List<Consumer<Long>> consumers = new ArrayList<>();

    SliderRow(Tab tab, Key key, String description, List<String> tooltip, int initialValue, int min, int max, ConfigScreen screen) {
        super(tab, key, description, tooltip, screen);
        this.initialValue = initialValue;
        this.min = min;
        this.max = max;

        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(key.getKey(), v);
            screen.getSaver().save(config);
            lastValue = v;
            getLabel().setMessage(Text.literal(getLabel().getMessage().getString()).setStyle(Radon.fontStyle.withItalic(false)));
        });
        reData();
    }

    SliderRow(Section section, Key key, String description, List<String> tooltip, int initialValue, int min, int max, ConfigScreen screen) {
        super(section, key, description, tooltip, screen);
        this.initialValue = initialValue;
        this.min = min;
        this.max = max;

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
     * Get the initial value of this row's slider
     * @return The initial value in {@link Integer}
     */
    public int getInitialValue() {
        return initialValue;
    }

    /**
     * Get the minimum value of this row's slider
     * @return The minimum value in {@link Integer}
     */
    public int getMin() {
        return min;
    }

    /**
     * Get the maximum value of this row's slider
     * @return The maximum value in {@link Integer}
     */
    public int getMax() {
        return max;
    }

    /**
     * Get the value of the input in this row
     * @return {@link String} which is the value
     */
    public long getValue() {
        return value;
    }

    private void setValue(long value, Boolean subscribe) {
        this.value = value;
        if (!initialized) return;
        slider.setValue((value - min) / (double)(max - min));
        slider.updateText(String.valueOf(value));
        if (subscribe) {
            for (Consumer<Long> consumer : consumers) {
                consumer.accept(this.value);
            }
        }
    }

    /**
     * Set the value of the button
     * @param value The {@link Long} value of the slider
     */
    public void setValue(long value) {
        setValue(value, true);
    }

    /**
     * Run code if the value of the slider changes
     * @param consumer A {@link Consumer} of {@link Long}
     */
    public void subscribe(Consumer<Long> consumer) {
        consumers.add(consumer);
    }

    /**
     * If instant save is disabled for the user, this will save the value of this row
     */
    @Override
    public void save() {
        if (Radon.instantSave) return;
        if (lastValue == value) return;
        for (Consumer<Long> consumer : consumers) {
            consumer.accept(this.value);
        }
    }

    @Override
    void reRender() {
        super.reRender();

        slider = new Slider(
                screen.width - 10 - 75,
                0,
                75,
                16,
                String.valueOf(this.value),
                s -> {
                    this.value = Math.round((s.getValue() * 100) / 100 * (max - min) + min);
                    s.updateText(String.valueOf(this.value));
                    if (Radon.instantSave) {
                        for (Consumer<Long> consumer : consumers) {
                            consumer.accept(this.value);
                        }
                    } else {
                        getLabel().setMessage(Text.literal(getLabel().getMessage().getString()).setStyle(Radon.fontStyle.withItalic(this.value != lastValue)));
                    }
                },
                Sound.MENU_SLIDE,
                Sound.MENU_CLICK,
                (this.value - min) / (double)(max - min)
        );

        ((IScreenMixin) screen).addDrawableChildPublic(slider);
        TickUtil.runNextTick(this::reData);
    }

    protected void reData() {
        JsonObject cfg = screen.getSaver().load();
        if (cfg != null) {
            JsonElement val = cfg.get(key.getKey());
            if (val == null) {
                cfg.addProperty(key.getKey(), initialValue);
            }
            long value = cfg.get(key.getKey()).getAsLong();
            if (value >= min && value <= max) {
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
     * Get the slider of the row
     * @return {@link Slider}
     */
    @Override
    public Widget getWidget() {
        return slider;
    }
}
