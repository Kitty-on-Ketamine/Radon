package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.DataUtils;
import me.kitty.radon.client.Sound;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WidgetDrawer {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static HashMap<UUID, Object> data = new HashMap<>();
    public static HashMap<UUID, Button> buttons = new HashMap<>();
    public static HashMap<UUID, Slider> sliders = new HashMap<>();
    private final static HashMap<Screen, Integer> heightOffset = new HashMap<>();
    private final static HashMap<Screen, Integer> scrollOffset = new HashMap<>();
    private record Collection(TextWidget label, Widget widget, Box box) {}
    private final static HashMap<Screen, ArrayList<Collection>> screenEntries = new HashMap<>();

    public static void removeOffset(Screen screen) {
        heightOffset.remove(screen);
        scrollOffset.remove(screen);
        screenEntries.remove(screen);
    }

    public static UUID addButtonRow(String text, List<String> description, Screen screen, Object option) {

        heightOffset.putIfAbsent(screen, 75);

        int sideOffset = 10;
        UUID uuid = UUID.randomUUID();

        if (option instanceof Boolean) {

            int height = heightOffset.get(screen);

            Box box = new Box(
                    sideOffset,
                    height,
                    screen.width - sideOffset,
                    height + 16,
                    0x33000000,
                    0xffffffff,
                    description
            );

            TextWidget label = new TextWidget(
                    sideOffset,
                    height + 1,
                    200,
                    16,
                    Text.literal(text).setStyle(Radon.fontStyle),
                    mc.textRenderer
            );

            Button button = new Button(
                    screen.width - sideOffset - 75,
                    height,
                    75,
                    16,
                    String.valueOf(option),
                    List.of(),
                    (Boolean) option ? 0xff55ff55 : 0xffff5555,
                    (button1) -> {

                        data.putIfAbsent(uuid, option);

                        boolean newValue = !(Boolean) data.get(uuid);

                        data.put(uuid, newValue);
                        button1.updateText(String.valueOf(newValue));
                        button1.updateColor(newValue ? 0xff55ff55 : 0xffff5555);

                    },
                    Sound.MENU_CLICK
            );
            buttons.put(uuid, button);

            ((IScreenMixin) screen).addDrawableChildPublic(box);
            ((IScreenMixin) screen).addDrawableChildPublic(button);
            ((IScreenMixin) screen).addDrawableChildPublic(label);

            screenEntries.putIfAbsent(screen, new ArrayList<>());
            screenEntries.get(screen).add(new Collection(label, button, box));

        } else if (option instanceof Enum<?> enumOption) {

            int height = heightOffset.get(screen);

            data.put(uuid, enumOption);

            Box box = new Box(
                    sideOffset,
                    height,
                    screen.width - sideOffset,
                    height + 16,
                    0x33000000,
                    0xffffffff,
                    description
            );

            TextWidget label = new TextWidget(
                    sideOffset,
                    height + 1,
                    200,
                    16,
                    Text.literal(text).setStyle(Radon.fontStyle),
                    mc.textRenderer
            );

            Button button = new Button(
                    screen.width - sideOffset - 75,
                    height,
                    75,
                    16,
                    String.valueOf(option),
                    List.of(),
                    0,
                    (button1) -> {

                        data.put(uuid, DataUtils.next((Enum<?>) data.get(uuid)));
                        button1.updateText(data.get(uuid).toString());

                    },
                    Sound.MENU_CLICK
            );

            ((IScreenMixin) screen).addDrawableChildPublic(box);
            ((IScreenMixin) screen).addDrawableChildPublic(button);
            ((IScreenMixin) screen).addDrawableChildPublic(label);

            screenEntries.putIfAbsent(screen, new ArrayList<>());
            screenEntries.get(screen).add(new Collection(label, button, box));

        }

        heightOffset.put(screen, heightOffset.get(screen) + 20);

        renderCollections(screen, screenEntries.get(screen));

        return uuid;

    }

    public static void updateButton(UUID uuid, Object option) {
        Button button = buttons.get(uuid);
        if (option instanceof Boolean) {
            data.put(uuid, option);
            button.updateText(String.valueOf(option));
            button.updateColor((Boolean) option ? 0xff55ff55 : 0xffff5555);
        } else if (option instanceof Enum<?> enumOption) {
            data.put(uuid, enumOption);
            button.updateText(enumOption.name());
        }
    }

    public static UUID addSliderRow(String text, List<String> description, Screen screen, int initialValue, int min, int max) {

        heightOffset.putIfAbsent(screen, 75);

        int sideOffset = 10;

        UUID uuid = UUID.randomUUID();
        int height = heightOffset.get(screen);

        data.put(uuid, initialValue);

        Box box = new Box(
                sideOffset,
                height,
                screen.width - sideOffset,
                height + 16,
                0x33000000,
                0xffffffff,
                description
        );

        TextWidget label = new TextWidget(
                sideOffset,
                height + 1,
                200,
                16,
                Text.literal(text).setStyle(Radon.fontStyle),
                mc.textRenderer
        );

        Slider slider = new Slider(
                screen.width - sideOffset - 75,
                height,
                75,
                16,
                String.valueOf(initialValue),
                (slider1) -> {

                    long value = Math.round((slider1.getValue() * 100) / 100 * (max - min) + min);

                    data.put(uuid, value);
                    slider1.updateText(String.valueOf(value));

                },
                Sound.MENU_SLIDE,
                Sound.MENU_CLICK,
                (initialValue - min) / (double)(max - min)
        );
        sliders.put(uuid, slider);

        ((IScreenMixin) screen).addDrawableChildPublic(box);
        ((IScreenMixin) screen).addDrawableChildPublic(slider);
        ((IScreenMixin) screen).addDrawableChildPublic(label);

        screenEntries.putIfAbsent(screen, new ArrayList<>());
        screenEntries.get(screen).add(new Collection(label, slider, box));

        heightOffset.put(screen, heightOffset.get(screen) + 20);

        renderCollections(screen, screenEntries.get(screen));

        return uuid;

    }

    public static void updateSlider(UUID uuid, double value, int min, int max) {
        Slider slider = sliders.get(uuid);
        long v = Math.round((value * 100) / 100 * (max - min) + min);

        data.put(uuid, v);
        slider.updateText(String.valueOf(v));
    }

    public static void end(Screen screen) {

        heightOffset.put(screen, 75);
        scrollOffset.putIfAbsent(screen, 0);

    }

    private static void renderCollections(Screen screen, ArrayList<Collection> collections) {

        scrollOffset.putIfAbsent(screen, 0);

        int startY = 75 - scrollOffset.get(screen);
        heightOffset.put(screen, startY);

        for (Collection collection : screenEntries.get(screen)) {

            int y = heightOffset.get(screen);

            heightOffset.put(screen, heightOffset.get(screen) + 20);

            if (y < 75 || y > screen.height - 60 || !collections.contains(collection)) {

                if (!collections.contains(collection)) {
                    heightOffset.put(screen, heightOffset.get(screen) - 20);
                }

                /*collection.box.visible = false;
                collection.label.visible = false;
                collection.widget.hidden = true;*/

                collection.box.y1 = -100;
                collection.box.y2 = -100;
                collection.label.setY(-100);
                collection.widget.setY(-100);

            } else {

                /*collection.box.visible = true;
                collection.label.visible = true;
                collection.widget.hidden = false;*/

                collection.box.y1 = y;
                collection.box.y2 = y + 16;
                collection.label.setY(y + 1);
                collection.widget.setY(y);

            }

        }

    }

    public static void search(Screen screen, String keyword) {

        if (!screenEntries.containsKey(screen)) return;

        if (keyword.isBlank()) {

            renderCollections(screen, screenEntries.get(screen));
            return;

        }

        ArrayList<Collection> found = new ArrayList<>();

        for (Collection collection : screenEntries.get(screen)) {

            String text = collection.label.getMessage().getString();

            if (text.toLowerCase().replaceAll(" ", "").contains(keyword.toLowerCase().replaceAll(" ", ""))) {

                found.add(collection);

            }

        }

        //if (found.isEmpty()) found = screenEntries.get(screen);

        renderCollections(screen, found);

    }

    private static int getMaxScroll(Screen screen) {

        if (!screenEntries.containsKey(screen)) return 0;

        int totalHeight = screenEntries.get(screen).size() * 20;

        int visibleHeight = screen.height - 100;

        return Math.max(0, totalHeight - visibleHeight);

    }

    public static void scroll(Screen screen, double amount) {

        scrollOffset.putIfAbsent(screen, 0);

        int current = scrollOffset.get(screen);

        int scrollSpeed = 10;

        current -= (int) (amount * scrollSpeed);

        int maxScroll = getMaxScroll(screen);

        if (current < 0) current = 0;
        if (current > maxScroll) current = maxScroll;

        scrollOffset.put(screen, current);

        renderCollections(screen, screenEntries.get(screen));

    }

}