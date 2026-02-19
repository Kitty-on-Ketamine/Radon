package me.kitty.radon.api;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.List;

public class InputRow extends Row {
    private String placeholder;
    private String value;
    private final Input input;

    InputRow(String description, List<String> tooltip, String placeholder, ConfigScreen screen) {
        super(description, tooltip, screen);
        this.placeholder = placeholder;

        input = new Input(
                screen.width - 10 - 75,
                screen.getHeightOffset() + 5,
                75,
                16,
                placeholder,
                i -> this.value = i.getText(),
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        );

        ((IScreenMixin) screen).addDrawableChildPublic(input);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        if (timestamp + 1000 > System.currentTimeMillis()) return;
        this.placeholder = placeholder;
        input.setPlaceholder(Text.literal(placeholder).setStyle(Radon.fontStyle));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (timestamp + 1000 > System.currentTimeMillis()) return;
        this.value = value;
        input.setText(value);
    }

    @Override
    public Widget getWidget() {
        return input;
    }
}
