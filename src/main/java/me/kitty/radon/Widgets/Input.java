package me.kitty.radon.Widgets;

import me.kitty.radon.client.Draw;
import me.kitty.radon.client.Sound;
import net.minecraft.client.MinecraftClient;
//? if >1.21.8 {
import net.minecraft.client.gui.Click;
//? }
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static me.kitty.radon.Radon.fontStyle;

public class Input extends TextFieldWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static Identifier TEXTURE_NORMAL = Identifier.of("radon", "widget/input");
    public static Identifier TEXTURE_HOVER = Identifier.of("radon", "widget/input_highlighted");

    private final Consumer<Input> onType;
    private final SoundEvent clickSound;
    private final SoundEvent typeSound;
    private final SoundEvent backSpaceSound;
    private final Text placeholder;
    private Boolean hidden;

    public Input(int x, int y, int width, int limit, String placeholder, Consumer<Input> onType, SoundEvent clickSound, SoundEvent typeSound, SoundEvent backSpaceSound) {

        super(mc.textRenderer, x + 4, y + 4, width - 10, 16, Text.empty());

        this.setPlaceholder(Text.literal(placeholder).withColor(0xFF656565));
        this.setDrawsBackground(false);
        this.setMaxLength(limit);
        this.setEditableColor(0xFF656565);

        this.hidden = false;

        this.onType = onType;
        this.clickSound = clickSound;
        this.typeSound = typeSound;
        this.backSpaceSound = backSpaceSound;
        this.placeholder = Text.literal(placeholder).setStyle(fontStyle);

    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        if (this.hidden) return;

        Identifier texture;

        if (this.isHovered()) {

            texture = TEXTURE_HOVER;
            this.setEditableColor(0xFFa1a1a1);

        } else {

            texture = TEXTURE_NORMAL;
            this.setEditableColor(0xFF656565);

        }

        Draw.drawGui(context, texture, getX() - 4, getY() - 4, width + 10, height);

        super.renderWidget(context, mouseX, mouseY, deltaTicks);

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
        super.onClick(mouseX, mouseY);
        Sound.play(clickSound);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        Sound.play(clickSound);
    }
    *///? }

    @Override
    public void write(String text) {

        Sound.play(typeSound);

        super.write(text);

        onType.accept(this);

    }

    @Override
    public void eraseCharacters(int characterOffset) {

        Sound.play(backSpaceSound);

        super.eraseCharacters(characterOffset);
        onType.accept(this);

    }

    public void hide() {

        hidden = true;

    }

}
