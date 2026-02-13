package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
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
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static me.kitty.radon.Radon.scaleMultiplier;

public class Button extends ClickableWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static final Identifier TEXTURE_NORMAL = Identifier.of("radon", "widgets/button");
    private static final Identifier TEXTURE_HOVER = Identifier.of("radon", "widgets/button_highlighted");
    private static final Identifier TEXTURE_DISABLED = Identifier.of("radon", "widgets/button_disabled");

    private final Consumer<Button> onPress;
    private final SoundEvent clickSound;
    private Text text;

    public Button(int x, int y, int width, int height, String text, Consumer<Button> onPress, SoundEvent clickSound) {

        super(x, y, width, height, Text.of(text));

        this.onPress = onPress;
        this.clickSound = clickSound;
        this.text = Text.literal(text).setStyle(Radon.fontStyle);

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        Identifier texture;
        int textColor;

        if (!this.active) {

            texture = TEXTURE_DISABLED;
            textColor = 0xFF404040;

        } else if (this.isHovered()) {
            
            texture = TEXTURE_HOVER;
            textColor = 0xFFa1a1a1;
            
        } else {

            texture = TEXTURE_NORMAL;
            textColor = 0xFF606060;

        }

        //?if >1.21.4 {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), width, height);
        //? } else {
        /*context.drawGuiTexture(RenderLayer::getGuiTextured, texture, getX(), getY(), width, height);
        *///? }
        context.drawCenteredTextWithShadow(mc.textRenderer, this.text, getX() + width / 2, getY() + (height - mc.textRenderer.fontHeight + 2) / 2, textColor);

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void playDownSound(SoundManager soundManager) {

        soundManager.play(PositionedSoundInstance.ui(clickSound, 1.0f, 5.0f * Radon.volume));

    }

    @Override
    public void onClick(Click click, boolean doubled) {

        onPress.accept(this);

    }

}
