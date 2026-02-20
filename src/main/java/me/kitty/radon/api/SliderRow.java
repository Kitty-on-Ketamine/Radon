package me.kitty.radon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Slider;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SliderRow extends Row {
    private final int initialValue;
    private final int min;
    private final int max;
    private long value;
    private Slider slider;
    private final List<Consumer<Long>> consumers = new ArrayList<>();

    SliderRow(String description, List<String> tooltip, int initialValue, int min, int max, ConfigScreen screen) {
        super(description, tooltip, screen);
        this.initialValue = initialValue;
        this.min = min;
        this.max = max;

        JsonObject cfg = screen.getSaver().load();
        if (cfg != null) {
            JsonElement val = cfg.get(description);
            if (val == null) {
                cfg.addProperty(description, initialValue);
            }
            long value = cfg.get(description).getAsLong();
            if (value >= min && value <= max) {
                TickUtil.runNextTick(() -> setValue(value, false));
            }
        }

        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(description, v);
            screen.getSaver().save(config);
        });
    }

    public int getInitialValue() {
        return initialValue;
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
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
    public void setValue(long value) {
        setValue(value, true);
    }
    public long getValue() {
        return value;
    }
    public void subscribe(Consumer<Long> consumer) {
        consumers.add(consumer);
    }

    @Override
    void reRender() {
        super.reRender();

        slider = new Slider(
                screen.width - 10 - 75,
                height,
                75,
                16,
                String.valueOf(initialValue),
                s -> {
                    this.value = Math.round((s.getValue() * 100) / 100 * (max - min) + min);
                    s.updateText(String.valueOf(this.value));
                    for (Consumer<Long> consumer : consumers) {
                        consumer.accept(this.value);
                    }
                },
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE,
                (initialValue - min) / (double)(max - min)
        );

        ((IScreenMixin) screen).addDrawableChildPublic(slider);
    }

    @Override
    public Widget getWidget() {
        return slider;
    }
}
