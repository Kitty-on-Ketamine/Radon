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
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class Slider extends SliderWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static final Identifier TEXTURE_NORMAL = Identifier.of("radon", "widgets/slider");
    private static final Identifier TEXTURE_HOVER = Identifier.of("radon", "widgets/slider_highlighted");
    private static final Identifier TEXTURE_HANDLE = Identifier.of("radon", "widgets/slider_handle");
    private static final Identifier TEXTURE_HANDLE_HOVER = Identifier.of("radon", "widgets/slider_handle_highlighted");

    private final Consumer<Slider> onPress;
    private final SoundEvent clickSound;
    private final SoundEvent slideSound;
    private Text text;
    private int textColor;
    private long now;
    private DrawContext drawContext;
    private Boolean hidden;

    public Slider(int x, int y, int width, int height, String text, Consumer<Slider> onPress, SoundEvent slideSound, SoundEvent clickSound, double initialValue) {

        super(x, y, width, height, Text.of(text), initialValue);

        this.onPress = onPress;
        this.clickSound = clickSound;
        this.slideSound = slideSound;
        this.text = Text.literal(text).setStyle(Radon.fontStyle);
        this.now = System.currentTimeMillis();

        this.hidden = false;

    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (this.hidden) return;

        drawContext = context;

        Identifier texture;
        Identifier handleTexture;

        if (this.isHovered()) {

            texture = TEXTURE_HOVER;
            handleTexture = TEXTURE_HANDLE_HOVER;
            textColor = 0xFFa1a1a1;

        } else {

            texture = TEXTURE_NORMAL;
            handleTexture = TEXTURE_HANDLE;
            textColor = 0xFF606060;

        }

        //?if >1.21.4 {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), width, height);

        int handleWidth = 9;
        int handleX = getX() + (int)(this.value * (width - handleWidth));
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, handleTexture, handleX, getY(), handleWidth, height);

        context.drawCenteredTextWithShadow(mc.textRenderer, Text.of(this.text), getX() + width / 2, getY() + (height - mc.textRenderer.fontHeight + 2) / 2, textColor);

        //? } else {
        /*context.drawGuiTexture(RenderLayer::getGuiTextured, texture, getX(), getY(), width, height);
         *///? }

    }

    @Override
    protected void updateMessage() {

    }

    @Override
    protected void applyValue() {

        if (System.currentTimeMillis() - this.now > 45) {

            mc.getSoundManager().play(PositionedSoundInstance.ui(slideSound, 1.0f, 5.0f * Radon.volume));
            this.now = System.currentTimeMillis();

        }

        onPress.accept(this);

    }

    @Override
    public void onClick(Click click, boolean doubled) {

        mc.getSoundManager().play(PositionedSoundInstance.ui(clickSound, 1.0f, 5.0f * Radon.volume));

    }

    public void updateText(String text) {
        this.text = Text.literal(text).setStyle(Radon.fontStyle);

        drawContext.drawCenteredTextWithShadow(mc.textRenderer, Text.of(text), getX() + width / 2, getY() + (height - 8) / 2, textColor);

    }

    @Override
    public void onRelease(Click click) {

        mc.getSoundManager().play(PositionedSoundInstance.ui(clickSound, 0.8f, 5.0f * Radon.volume));

    }

    public double getValue() {

        return this.value;

    }

    public void hide() {

        hidden = true;

    }

}
