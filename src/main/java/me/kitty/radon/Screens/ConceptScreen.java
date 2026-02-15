package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.*;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class ConceptScreen extends Screen {

    MinecraftClient mc = MinecraftClient.getInstance();

    String title;
    Screen origin;

    public ConceptScreen(String title, Screen origin) {

        super(Text.literal(title));

        this.title = title;
        this.origin = origin;

    }

    @Override
    public void init() {

        addDrawableChild(new Button(
                width - 50 - 10,
                5,
                50,
                16,
                "Radon",
                List.of(),
                0,
                (button) -> { mc.execute(() -> mc.setScreen(new ModMenu()));},
                Sound.MENU_CLICK
        ));

        addDrawableChild(new Button(
                10,
                5,
                50,
                16,
                "Back",
                List.of(),
                0,
                (button) -> { mc.execute(() -> mc.setScreen(this.origin));},
                Sound.MENU_CLICK
        ));

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));
        addDrawableChild(new StaticBox(-2, height + 4, width + 2, height - 40, 0x33000000,  0xffffffff, List.of()));

        addDrawableChild(new Input(
                10,
                height - 16 - 10,
                75,
                15,
                "Search...",
                (input) -> {

                    WidgetDrawer.search(this, input.getText());

                    },
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        ));

        WidgetDrawer.addButtonRow("Enable Mod", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Enable Mod", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Enable Mod", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Enable Mod", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Enable Mod", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 1", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 2", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 3", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow uwu", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Meow 5", List.of("meow"), this, true);

        WidgetDrawer.end(this);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, Text.literal(this.title).setStyle(Radon.fontStyle), width / 2, 15, 0xFFFFFFFF);

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {

        WidgetDrawer.scroll(this, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

    }

}
