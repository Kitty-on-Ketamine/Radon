package me.kitty.radon.api;

import me.kitty.radon.Widgets.WidgetDrawer;

import java.util.List;
import java.util.UUID;

public class SliderRow extends Row {
    private final int initialValue;
    private final int min;
    private final int max;
    private final UUID uuid;
    private final long timestamp;

    SliderRow(String description, List<String> tooltip, int initialValue, int min, int max, ConfigScreen screen) {
        super(description, tooltip, screen);
        uuid = WidgetDrawer.addSliderRow(description, tooltip, screen, initialValue, min, max);
        this.initialValue = initialValue;
        this.min = min;
        this.max = max;
        this.timestamp = System.currentTimeMillis();
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
    public void setValue(int value) {
        if (timestamp + 1000 > System.currentTimeMillis()) return;
        WidgetDrawer.updateSlider(uuid, value, min, max);
    }
}
