package me.kitty.radon.Utils;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class CursorHelper {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public enum Cursors {

        NORMAL,
        SLIDER,
        POINTER

    }

    public static void setCursor(Cursors newCursor) {

        long handle = mc.getWindow().getHandle();
        long cursor = -1;

        switch (newCursor) {

            case Cursors.NORMAL:
                cursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
                break;

            case Cursors.POINTER:
                cursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
                break;

            case Cursors.SLIDER:
                cursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
                break;

            default:
                cursor = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
                break;

        }

        GLFW.glfwSetCursor(handle, cursor);

    }

}
