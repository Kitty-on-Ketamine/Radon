package me.kitty.radon.mixin;

import me.kitty.radon.client.IScreenMixin;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.client.gui.screen.Screen.class)
public abstract class ScreenMixin implements IScreenMixin {

    @Shadow
    protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement);

    public <T extends Element & Drawable & Selectable> T addDrawableChildPublic(T drawableElement) {

        return addDrawableChild(drawableElement);

    }

}