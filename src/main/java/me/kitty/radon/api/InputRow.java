package me.kitty.radon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.kitty.radon.Radon;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Input;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InputRow extends Row {
    private String placeholder;
    private String value;
    private Input input;
    private final List<Consumer<String>> consumers = new ArrayList<>();
    private final int limit;

    InputRow(String description, List<String> tooltip, String placeholder, int limit, ConfigScreen screen) {
        super(description, tooltip, screen);
        this.placeholder = placeholder;
        this.limit = limit;

        JsonObject cfg = screen.getSaver().load();
        if (cfg != null) {
            JsonElement val = cfg.get(description);
            if (val == null) {
                cfg.addProperty(description, "");
            }
            String value = cfg.get(description).getAsString();
            if (!value.isEmpty() && value.length() <= limit) {
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

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        if (!initialized) return;
        this.placeholder = placeholder;
        input.setPlaceholder(Text.literal(placeholder).setStyle(Radon.fontStyle));
    }

    @Override
    void reRender() {
        super.reRender();

        input = new Input(
                screen.width - 10 - 75,
                height,
                75,
                limit,
                placeholder,
                i -> {
                    this.value = i.getText();
                    for (Consumer<String> consumer : consumers) {
                        consumer.accept(this.value);
                    }
                },
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        );

        ((IScreenMixin) screen).addDrawableChildPublic(input);
    }

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

    public void setValue(String value) {
        setValue(value, true);
    }

    public int getLimit() {
        return limit;
    }

    public void subscribe(Consumer<String> consumer) {
        consumers.add(consumer);
    }

    @Override
    public Widget getWidget() {
        return input;
    }
}
