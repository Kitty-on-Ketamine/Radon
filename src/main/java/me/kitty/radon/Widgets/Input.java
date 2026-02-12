package me.kitty.radon.Widgets;

import net.minecraft.client.MinecraftClient;
//? if >1.21.4 {
import net.minecraft.client.gl.RenderPipelines;
//? } else {
/*import net.minecraft.client.render.RenderLayer;
 *///? }
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Input extends ClickableWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static final Identifier TEXTURE_NORMAL = Identifier.of("radon", "widgets/input");
    private static final Identifier TEXTURE_HOVER = Identifier.of("radon", "widgets/input_highlighted");
    private static final Identifier TEXTURE_DISABLED = Identifier.of("radon", "widgets/input_disabled");

    private final Runnable onPress;
    private final SoundEvent clickSound;
    private final String placeholder;

    public Input(int x, int y, int width, int height, String placeholder, Runnable onPress, SoundEvent clickSound) {

        super(x, y, width, height, Text.of(placeholder));

        this.onPress = onPress;
        this.clickSound = clickSound;
        this.placeholder = placeholder;

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        Identifier texture;
        int textColor;

        if (!this.active) {

            texture = TEXTURE_DISABLED;
            textColor = 0xFF404040;

        }
        else if (this.isHovered()) {

            texture = TEXTURE_HOVER;
            textColor = 0xFFa1a1a1;

        }
        else {

            texture = TEXTURE_NORMAL;
            textColor = 0xFF606060;

        }

        //?if >1.21.4 {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), width, height);
        //? } else {
        /*context.drawGuiTexture(RenderLayer::getGuiTextured, texture, getX(), getY(), width, height);
         *///? }
        context.drawCenteredTextWithShadow(mc.textRenderer, Text.of(this.placeholder), getX() + width / 2, getY() + (height - 8) / 2, textColor);

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

}
