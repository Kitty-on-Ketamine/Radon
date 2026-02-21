package me.kitty.radon.client;

//? if >1.21.5 {
/*import net.minecraft.client.gl.RenderPipelines;
*///? } else if >1.21.1 {
/*import net.minecraft.client.render.RenderLayer;
*///? }
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class Draw {
    public static void drawGui(DrawContext drawContext, Identifier texture, int x, int y, int width, int height) {
        drawContext.drawGuiTexture(
                //? if >1.21.5 {
                /*RenderPipelines.GUI_TEXTURED,
                *///? } else if >1.21.1 {
                /*RenderLayer::getGuiTextured,
                *///? }
                texture,
                x, y,
                width, height
        );
    }
    public static void draw(DrawContext drawContext, Identifier texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        drawContext.drawTexture(
                //? if >1.21.5 {
                /*RenderPipelines.GUI_TEXTURED,
                *///? } else if >1.21.1 {
                /*RenderLayer::getGuiTextured,
                 *///? }
                texture,
                x, y,
                u, v,
                width, height,
                textureWidth, textureHeight
        );
    }
}
