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
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.function.Consumer;

public class Button extends ClickableWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static final Identifier TEXTURE_NORMAL = Identifier.of("radon", "widgets/button");
    private static final Identifier TEXTURE_HOVER = Identifier.of("radon", "widgets/button_highlighted");
    private static final Identifier TEXTURE_DISABLED = Identifier.of("radon", "widgets/button_disabled");

    private final Consumer<Button> onPress;
    private final SoundEvent clickSound;
    private final int color;
    public Text text;
    public Boolean hidden;

    public int x;
    public int y;

    public Button(int x, int y, int width, int height, String text, @UnknownNullability List<String> description, int color, Consumer<Button> onPress, SoundEvent clickSound) {

        super(x, y, width, height, Text.of(text));

        this.onPress = onPress;
        this.clickSound = clickSound;
        this.text = Text.literal(text).setStyle(Radon.fontStyle);
        this.color = color;

        this.x = x;
        this.y = y;

        this.hidden = false;

        String tooltip = "";

        for (String line : description) {

            boolean check = description.indexOf(line) >= description.size();

            tooltip = tooltip + line + (check ? "" : "\n");

        }

        if (!description.isEmpty()) this.setTooltip(Tooltip.of(Text.literal(tooltip).setStyle(Radon.fontStyle)));

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (this.hidden) return;

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

        if (this.color != 0) {

            textColor = this.color;

        }

        //?if >1.21.4 {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, this.x, this.y, width, height);
        //? } else {
        /*context.drawGuiTexture(RenderLayer::getGuiTextured, texture, this.x, this.y, width, height);
        *///? }
        context.drawCenteredTextWithShadow(mc.textRenderer, this.text, this.x + width / 2, this.y + (height - mc.textRenderer.fontHeight + 2) / 2, textColor);

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

    public void hide() {

        hidden = true;

    }

}
