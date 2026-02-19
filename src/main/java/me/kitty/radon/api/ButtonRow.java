package me.kitty.radon.api;

import me.kitty.radon.Widgets.WidgetDrawer;

import java.util.List;
import java.util.UUID;

public class ButtonRow extends Row {
    private Object value;
    private final UUID uuid;
    private final long timestamp;

    ButtonRow(String description, List<String> tooltip, Object value, ConfigScreen screen) {
        super(description, tooltip, screen);
        uuid = WidgetDrawer.addButtonRow(description, tooltip, screen, value);
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        if (timestamp + 1000 > System.currentTimeMillis()) return;
        this.value = value;
        WidgetDrawer.updateButton(uuid, value);
    }
}
