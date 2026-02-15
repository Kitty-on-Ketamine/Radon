package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.client.Sound;
import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WidgetDrawer {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static HashMap<UUID, Object> data = new HashMap<>();
    private static HashMap<Screen, Integer> heightOffset = new HashMap<>();
    private record Collection(TextWidget label, Button button, Box box) {}
    private static HashMap<Screen, ArrayList<Collection>> screenEntries = new HashMap<>();

    public static void addButtonRow(String text, List<String> description, Screen screen, Object option) {

        heightOffset.putIfAbsent(screen, 75);

        int sideOffset = 10;

        if (option instanceof Boolean) {

            UUID uuid = UUID.randomUUID();
            int height = heightOffset.get(screen);

            data.put(uuid, option);

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

                        boolean newValue = !(Boolean) data.get(uuid);

                        data.put(uuid, newValue);
                        button1.text = Text.literal(String.valueOf(newValue)).setStyle(Radon.fontStyle).withColor(newValue ? 0xff55ff55 : 0xffff5555);

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

    }

    public static void end(Screen screen) {

         heightOffset.put(screen, 75);

    }

    private static void renderCollections(Screen screen, ArrayList<Collection> collections) {

        heightOffset.put(screen, 75);

        for (Collection collection : screenEntries.get(screen)) {

            collection.box.visible = false;
            collection.button.hidden = true;
            collection.label.visible = false;

        }

        for (Collection collection : collections) {

            collection.box.visible = true;
            collection.label.visible = true;
            collection.button.hidden = false;

            collection.box.y1 = heightOffset.get(screen);
            collection.box.y2 = heightOffset.get(screen) + 16;
            collection.label.setY(heightOffset.get(screen) + 1);
            collection.button.y = heightOffset.get(screen);

            heightOffset.put(screen, heightOffset.get(screen) + 20);

        }

        heightOffset.put(screen, 75);

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

        if (found.isEmpty()) found = screenEntries.get(screen);

        renderCollections(screen, found);

    }

}
