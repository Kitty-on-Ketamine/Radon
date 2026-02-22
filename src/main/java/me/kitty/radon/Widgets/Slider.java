package me.kitty.radon.Widgets;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.CursorHelper;
import me.kitty.radon.client.Draw;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
//? if >1.21.8 {
import net.minecraft.client.gui.Click;
//? }
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class Slider extends SliderWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static Identifier TEXTURE_NORMAL = Identifier.of("radon", "widget/slider");
    public static Identifier TEXTURE_HOVER = Identifier.of("radon", "widget/slider_highlighted");
    public static Identifier TEXTURE_HANDLE = Identifier.of("radon", "widget/slider_handle");
    public static Identifier TEXTURE_HANDLE_HOVER = Identifier.of("radon", "widget/slider_handle_highlighted");

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

            CursorHelper.setCursor(CursorHelper.Cursors.SLIDER);

        } else {

            texture = TEXTURE_NORMAL;
            handleTexture = TEXTURE_HANDLE;
            textColor = 0xFF606060;

            CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);

        }

        Draw.drawGui(context, texture, getX(), getY(), width, height);
        int handleWidth = 9;
        int handleX = getX() + (int)(this.value * (width - handleWidth));
        Draw.drawGui(context, handleTexture, handleX, getY(), handleWidth, height);
        context.drawCenteredTextWithShadow(mc.textRenderer, this.text, getX() + width / 2, getY() + (height - mc.textRenderer.fontHeight + 2) / 2, textColor);

    }

    @Override
    protected void updateMessage() {

    }

    @Override
    protected void applyValue() {

        if (System.currentTimeMillis() - this.now > 45) {

            Sound.play(slideSound);
            this.now = System.currentTimeMillis();

        }

        onPress.accept(this);

    }

    //?if >1.21.8 {
    @Override
    public void onClick(Click click, boolean doubled) {
        Sound.play(clickSound);
    }

    @Override
    public void onRelease(Click click) {
        Sound.play(clickSound);
    }
     //? } else {
    /*@Override
    public void onClick(double mouseX, double mouseY) {
        Sound.play(clickSound);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        Sound.play(clickSound);
    }
    *///? }

    public void updateText(String text) {
        this.text = Text.literal(text).setStyle(Radon.fontStyle);
    }

    public double getValue() {

        return this.value;

    }

    public void setValue(double value) {
        this.value = value;
        updateMessage();
        applyValue();
    }

    public void hide() {

        hidden = true;

    }

}
