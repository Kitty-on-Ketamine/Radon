package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.*;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

import static me.kitty.radon.Radon.*;

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

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of(), new StaticBox.Icons(100, static_bg, 0, static_bg)));
        addDrawableChild(new StaticBox(-2, height + 4, width + 2, height - 40, 0x33000000,  0xffffffff, List.of(), new StaticBox.Icons(100, static_bg, 0, static_bg)));

        addDrawableChild(new Button(
                width - 50 - 10,
                5,
                50,
                16,
                "Radon",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new ModMenu())),
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
                (button) -> mc.execute(() -> mc.setScreen(this.origin)),
                Sound.MENU_CLICK
        ));

        addDrawableChild(new Input(
                10,
                height - 16 - 10,
                75,
                15,
                "Search...",
                (input) -> WidgetDrawer.search(this, input.getText()),
                Sound.MENU_CLICK,
                Sound.MENU_CLICK,
                Sound.MENU_SLIDE
        ));

        enum cica {

            meow,
            mreow1,
            mreow2,
            mreow3,
            mreow4,
            mreow5,

        }

        WidgetDrawer.addSliderRow("Meow 5", List.of("meow"), this, 83, 10, 110);
        WidgetDrawer.addButtonRow("Enable Mod", List.of("meow"), this, true);
        WidgetDrawer.addButtonRow("Enable Mod", List.of("uwu"), this, cica.mreow1);
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

        this.addDrawableChild(new RectBox(
                this.width / 2 - 30,
                36,
                this.width / 2 + 30,
                55,
                false,
                false,
                true,
                true,
                0x880000,
                0xffffffff,
                List.of(),
                true
        ));

        this.addDrawableChild(new TextWidget(
                this.width / 2 - textRenderer.getWidth("teszt") / 2,
                34 + textRenderer.fontHeight / 3 * 2,
                60,
                textRenderer.fontHeight,
                Text.literal("teszt").setStyle(Radon.fontStyle),
                textRenderer
        ));

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, Text.literal(this.title).setStyle(Radon.fontStyle), width / 2, 15, 0xFFFFFFFF);

    }

    @Override
    public void resize(
            //? if <1.21.11 {
            /*MinecraftClient client,
            *///? }
            int width,
            int height
    ) {
        super.resize(
                //? if <1.21.11 {
                /*client,
                *///? }
                width,
                height
        );
        mc.execute(() -> mc.setScreen(new ConceptScreen(title, origin)));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {

        WidgetDrawer.scroll(this, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

    }

}
