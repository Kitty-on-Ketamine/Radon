package me.kitty.radon.Widgets;

import net.minecraft.client.MinecraftClient;
//? if >1.21.4 {
import net.minecraft.client.gl.RenderPipelines;
//?}
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Button extends ClickableWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static final Identifier TEXTURE_NORMAL = Identifier.of("radon", "button");
    private static final Identifier TEXTURE_HOVER = Identifier.of("radon", "button_highlighted");
    private static final Identifier TEXTURE_DISABLED = Identifier.of("radon", "button_disabled");

    private final Runnable onPress;
    private final SoundEvent clickSound;
    private final String text;

    public Button(int x, int y, int width, int height, String text, Runnable onPress, SoundEvent clickSound) {

        super(x, y, width, height, Text.of(text));

        this.onPress = onPress;
        this.clickSound = clickSound;
        this.text = text;

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        Identifier texture;
        int textColor;

        if (!this.active) {

            texture = TEXTURE_DISABLED;
            textColor = 0x383838;

        }
        else if (this.isHovered()) {
            
            texture = TEXTURE_HOVER;
            textColor = 0x919191;
            
        }
        else {

            texture = TEXTURE_NORMAL;
            textColor = 0x505050;

        }

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), width, height);
        context.drawCenteredTextWithShadow(mc.textRenderer, Text.of(this.text), getX() + width / 2, getY() + (height - 8) / 2, textColor);

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

}
