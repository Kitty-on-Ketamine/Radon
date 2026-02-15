package me.kitty.radon.client;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

public interface IScreenMixin {

    <T extends Element & Drawable & Selectable> T addDrawableChildPublic(T drawableElement);

}