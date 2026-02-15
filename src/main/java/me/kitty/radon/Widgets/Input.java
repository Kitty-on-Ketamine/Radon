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
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static me.kitty.radon.Radon.fontStyle;

public class Input extends TextFieldWidget {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private static final Identifier TEXTURE_NORMAL = Identifier.of("radon", "widgets/input");
    private static final Identifier TEXTURE_HOVER = Identifier.of("radon", "widgets/input_highlighted");
    private static final Identifier TEXTURE_DISABLED = Identifier.of("radon", "widgets/input_disabled");

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

        if (!this.active) texture = TEXTURE_DISABLED;
        else if (this.isHovered()) {

            texture = TEXTURE_HOVER;
            this.setEditableColor(0xFFa1a1a1);

        }
        else {

            texture = TEXTURE_NORMAL;
            this.setEditableColor(0xFF656565);

        }

        //?if >1.21.4 {
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, texture, getX() - 4, getY() - 4, width + 10, height);
        //? } else {
        /*context.drawGuiTexture(RenderLayer::getGuiTextured, texture, getX(), getY(), width, height);
         *///? }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);

    }

    @Override
    public void onClick(Click click, boolean doubled) {

        mc.getSoundManager().play(PositionedSoundInstance.ui(clickSound, 1.0f, 5.0f * Radon.volume));

    }

    @Override
    public void onRelease(Click click) {

        mc.getSoundManager().play(PositionedSoundInstance.ui(clickSound, 0.8f, 5.0f * Radon.volume));

    }

    @Override
    public void write(String text) {

        mc.getSoundManager().play(PositionedSoundInstance.ui(typeSound, 1.0f, 5.0f * Radon.volume));
        onType.accept(this);

        super.write(text);

    }

    @Override
    public void eraseCharacters(int characterOffset) {

        mc.getSoundManager().play(PositionedSoundInstance.ui(backSpaceSound, 1.0f, 5.0f * Radon.volume));
        onType.accept(this);

        super.eraseCharacters(characterOffset);

    }

    public void hide() {

        hidden = true;

    }

}
