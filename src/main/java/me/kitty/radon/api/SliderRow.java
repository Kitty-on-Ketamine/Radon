package me.kitty.radon.api;

import me.kitty.radon.Widgets.Slider;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;

import java.util.List;

public class SliderRow extends Row {
    private final int initialValue;
    private final int min;
    private final int max;
    private long value;
    private final Slider slider;

    SliderRow(String description, List<String> tooltip, int initialValue, int min, int max, ConfigScreen screen) {
        super(description, tooltip, screen);
        this.initialValue = initialValue;
        this.min = min;
        this.max = max;

        slider = new Slider(
                screen.width - 10 - 75,
                screen.getHeightOffset(),
                75,
                16,
                String.valueOf(initialValue),
                s -> {
                    this.value = Math.round((s.getValue() * 100) / 100 * (max - min) + min);
                    s.updateText(String.valueOf(this.value));
                },
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE,
                (initialValue - min) / (double)(max - min)
        );

        ((IScreenMixin) screen).addDrawableChildPublic(slider);
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
        this.value = value;
        slider.updateText(String.valueOf(value));
    }

    @Override
    public Widget getWidget() {
        return slider;
    }
}
