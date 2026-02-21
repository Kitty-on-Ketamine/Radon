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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ButtonRow extends Row {
    private Object value;
    private Object lastValue;
    private Button button;
    private final List<Consumer<Object>> consumers = new ArrayList<>();

    ButtonRow(String description, List<String> tooltip, Object value, ConfigScreen screen) {
        super(description, tooltip, screen);
        this.value = value;

        subscribe(v -> {
            JsonObject config = screen.getSaver().load();
            if (config == null) return;
            config.addProperty(description, v.toString());
            screen.getSaver().save(config);
            lastValue = v;
        });
        reData();
    }
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
    public void setValue(Object value) {
        setValue(value, true);
    }
    public void subscribe(Consumer<Object> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void save() {
        if (lastValue == value) return;
        for (Consumer<Object> consumer : consumers) {
            consumer.accept(this.value);
        }
    }

    protected void reData() {
        JsonObject cfg = screen.getSaver().load();
        if (cfg != null) {
            JsonElement val = cfg.get(description);
            if (value instanceof Boolean) {
                if (val == null) {
                    cfg.addProperty(description, ((Boolean) value));
                }
                boolean v = cfg.get(description).getAsBoolean();
                setValue(v, false);
            } else if (value instanceof Enum<?> enumOption) {
                if (val == null) {
                    cfg.addProperty(description, "");
                }
                String v = cfg.get(description).getAsString();
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
                        }
                    },
                    Sound.MENU_CLICK
            );
        } else throw new IllegalArgumentException();

        ((IScreenMixin) screen).addDrawableChildPublic(button);
        TickUtil.runNextTick(this::reData);
    }

    @Override
    public Widget getWidget() {

        return button;

    }

}
