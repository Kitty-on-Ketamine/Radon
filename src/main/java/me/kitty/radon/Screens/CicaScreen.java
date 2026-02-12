package me.kitty.radon.Screens;

import me.kitty.radon.Widgets.Button;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static me.kitty.radon.client.Sound.MENU_CLICK;

public class CicaScreen extends Screen {

    public CicaScreen(String text) {

        super(Text.literal(text));

    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        Button button = new Button(
                centerX - 50,
                centerY - 10,
                100,
                20,
                "cicuska text !!!",

                () -> System.out.println("kitty katt"),
                MENU_CLICK
        );

        this.addDrawableChild(button);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {

        super.render(context, mouseX, mouseY, partialTicks);

        context.drawCenteredTextWithShadow(textRenderer, this.title, width / 2, height / 2, 0xFFFFFF);

    }

}
