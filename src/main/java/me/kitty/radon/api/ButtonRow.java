package me.kitty.radon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.kitty.radon.Radon;
import me.kitty.radon.Utils.DataUtils;
import me.kitty.radon.Utils.TickUtil;
import me.kitty.radon.Widgets.Button;
import me.kitty.radon.client.IScreenMixin;
import me.kitty.radon.client.Sound;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ButtonRow extends Row {
    private Object value;
    private Object lastValue;
    private Button button;
    private final List<Consumer<Object>> consumers = new ArrayList<>();

    ButtonRow(Tab tab, Key key, String description, List<String> tooltip, Object value, ConfigScreen screen) {
        super(tab, key, description, tooltip, screen);
        this.value = value;

        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(key.getKey(), v.toString());
            screen.getSaver().save(config);
            lastValue = v;
            getLabel().setMessage(Text.literal(getLabel().getMessage().getString()).setStyle(Radon.fontStyle.withItalic(false)));
        });
        reData();
    }

    ButtonRow(Section section, Key key, String description, List<String> tooltip, Object value, ConfigScreen screen) {
        super(section, key, description, tooltip, screen);
        this.value = value;

        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(key.getKey(), v.toString());
            screen.getSaver().save(config);
            lastValue = v;
            getLabel().setMessage(Text.literal(getLabel().getMessage().getString()).setStyle(Radon.fontStyle.withItalic(false)));
        });
        reData();

    }

    /**
     * Get the value of the button in this row
     * @return {@link Object} which is either a boolean or an enum
     */
    public Object getValue() {
        return value;
    }

    private void setValue(Object value, Boolean subscribe) {
        this.value = value;
        if (!initialized) return;
        if (this.value instanceof Boolean v) {
            button.updateText(String.valueOf(v));
            button.updateColor(v ? 0xff55ff55 : 0xffff5555);
        } else if (this.value instanceof Enum<?> enumOption) {
            button.updateText(enumOption.name());
        }
        if (subscribe) {
            for (Consumer<Object> consumer : consumers) {
                consumer.accept(this.value);
            }
        }
    }

    /**
     * Set the value of the button
     * @param value The {@link Boolean} value of the button
     */
    public void setValue(boolean value) {
        setValue(value, true);
    }

    /**
     * Set the value of the button
     * @param value The {@link Enum} value of the button
     */
    public void setValue(Enum<?> value) {
        setValue(value, true);
    }

    /**
     * Run code if the value of the button changes
     * @param consumer A {@link Consumer} of {@link Object}
     */
    public void subscribe(Consumer<Object> consumer) {
        consumers.add(consumer);
    }

    /**
     * If instant save is disabled for the user, this will save the value of this row
     */
    @Override
    public void save() {
        if (Radon.instantSave) return;
        if (lastValue == value) return;
        for (Consumer<Object> consumer : consumers) {
            consumer.accept(this.value);
        }
    }

    @Override
    void reRender() {
        super.reRender();

        if (this.value instanceof Boolean) {
            button = new Button(
                    screen.width - 10 - 75,
                    0,
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
                        if (Radon.instantSave) {
                            for (Consumer<Object> consumer : consumers) {
                                consumer.accept(this.value);
                            }
                        } else {
                            getLabel().setMessage(Text.literal(getLabel().getMessage().getString()).setStyle(Radon.fontStyle.withItalic(this.value != lastValue)));
                        }
                    },
                    Sound.MENU_CLICK
            );
        } else if (this.value instanceof Enum<?> enumOption) {
            button = new Button(
                    screen.width - 10 - 75,
                    0,
                    75,
                    16,
                    String.valueOf(enumOption),
                    List.of(),
                    0,
                    b -> {
                        this.value = DataUtils.next((Enum<?>) this.value);
                        b.updateText(String.valueOf(this.value));
                        if (Radon.instantSave) {
                            for (Consumer<Object> consumer : consumers) {
                                consumer.accept(this.value);
                            }
                        } else {
                            getLabel().setMessage(Text.literal(getLabel().getMessage().getString()).setStyle(Radon.fontStyle.withItalic(this.value != lastValue)));
                        }
                    },
                    Sound.MENU_CLICK
            );
        } else throw new IllegalArgumentException();

        ((IScreenMixin) screen).addDrawableChildPublic(button);
        TickUtil.runNextTick(this::reData);
    }

    protected void reData() {
        JsonObject cfg = screen.getSaver().load();
        if (cfg != null) {
            JsonElement val = cfg.get(key.getKey());
            if (value instanceof Boolean) {
                if (val == null) {
                    cfg.addProperty(key.getKey(), ((Boolean) value));
                }
                boolean v = cfg.get(key.getKey()).getAsBoolean();
                setValue(v, false);
            } else if (value instanceof Enum<?> enumOption) {
                if (val == null) {
                    cfg.addProperty(key.getKey(), "");
                }
                String v = cfg.get(key.getKey()).getAsString();
                if (!v.isEmpty()) {
                    Class<Enum> enumClass = (Class<Enum>) enumOption.getClass();
                    Enum<?> e = Enum.valueOf(enumClass, v);
                    setValue(e, false);
                }
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
     * Get the button of the row
     * @return {@link Button}
     */
    @Override
    public Widget getWidget() {
        return button;
    }
}
