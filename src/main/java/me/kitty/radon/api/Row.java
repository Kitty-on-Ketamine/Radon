package me.kitty.radon.api;

import java.util.List;

public class Row {
    private final String description;
    private final List<String> tooltipTexts;

    Row(String description, List<String> tooltip, ConfigScreen screen) {
        this.description = description;
        this.tooltipTexts = tooltip;
    }

    public String getDescription() {
        return description;
    }
    public List<String> getTooltipTexts() {
        return tooltipTexts;
    }
}
