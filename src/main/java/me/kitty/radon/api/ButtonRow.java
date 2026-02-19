package me.kitty.radon.api;

import me.kitty.radon.Utils.DataUtils;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;

import java.util.List;

public class ButtonRow extends Row {
    private Object value;
    private final Button button;

    ButtonRow(String description, List<String> tooltip, Object value, ConfigScreen screen) {
        super(description, tooltip, screen);
        this.value = value;
        if (this.value instanceof Boolean) {
            button = new Button(
                    screen.width - 10 - 75,
                    screen.getHeightOffset(),
                    75,
                    16,
                    String.valueOf(this.value),
                    List.of(),
                    (boolean) this.value ? 0xff55ff55 : 0xffff5555,
                    b -> {
                        boolean val = (boolean) this.value;
                        this.value = !val;
                        b.updateText(String.valueOf(!val));
                        b.updateColor(!val ? 0xff55ff55 : 0xffff5555);
                    },
                    Sound.MENU_CLICK
            );
        } else if (this.value instanceof Enum<?> enumOption) {
            button = new Button(
                    screen.width - 10 - 75,
                    screen.getHeightOffset(),
                    75,
                    16,
                    String.valueOf(enumOption),
                    List.of(),
                    0,
                    b -> {
                        this.value = DataUtils.next(enumOption);
                        b.updateText(String.valueOf(this.value));
                    },
                    Sound.MENU_CLICK
            );
        } else throw new IllegalArgumentException();
        ((IScreenMixin) screen).addDrawableChildPublic(button);
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        if (timestamp + 1000 > System.currentTimeMillis()) return;
        this.value = value;
        if (this.value instanceof Boolean v) {
            button.updateText(String.valueOf(!v));
            button.updateColor(!v ? 0xff55ff55 : 0xffff5555);
        } else if (this.value instanceof Enum<?> enumOption) {
            button.updateText(String.valueOf(enumOption));
        }
    }

    @Override
    public Widget getWidget() {
        return button;
    }
}
