package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Utils.CursorHelper;
import me.kitty.radon.Widgets.*;
import me.kitty.radon.api.ConfigScreen;
import me.kitty.radon.client.Draw;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static me.kitty.radon.client.Sound.MENU_CLICK;
import static me.kitty.radon.Radon.*;

public class ModMenu extends Screen {

    MinecraftClient mc = MinecraftClient.getInstance();
    private final Screen parent;

    public ModMenu(Screen parent) {

        super(Text.of(""));
        this.parent = parent;

    }

    public ModMenu() {
        this(null);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

        super.render(context, mouseX, mouseY, deltaTicks);

        int offset = 0;

        for (EntrypointContainer<ConfigScreen> container : FabricLoader.getInstance().getEntrypointContainers("radon", ConfigScreen.class)) {
            ModContainer mod = container.getProvider();
            Optional<String> iconPathOpt = mod.getMetadata().getIconPath(19);

            if (iconPathOpt.isPresent()) {
                Identifier iconId = Identifier.of("radon", "icons/" + mod.getMetadata().getId());
                Draw.draw(
                        context,
                        iconId,
                        50 + offset - 22,
                        50 - 6,
                        0, 0,
                        19, 19,
                        19, 19
                );
            }

            offset += 50;
        }
    }

    //? if >1.21.8 {
    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (var child : this.children()) {
            if (child instanceof RectBox box) {
                if (box.mouseClicked(click, doubled)) return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }
    //? } else {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (var child : this.children()) {
            if (child instanceof RectBox box) {
                if (box.mouseClicked(mouseX, mouseY, button)) return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
    *///? }

    @Override
    protected void init() {

        if (!defaultBackground) {
            addDrawableChild(new StaticBox(-2, -2, width + 2, height + 2, 0x33000000, 0xffffffff, List.of(), new StaticBox.Icons(95, bg, 5, coal)));
        }
        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of(), new StaticBox.Icons(100, static_bg, 0, static_bg)));

        Button backButton = new Button(
                10,
                5,
                50,
                16,
                parent != null ? "Back" : "Exit",
                List.of(),
                Radon.defaultTextures ? 0xffffffff : 0,
                (button) -> {
                    if (parent != null && parent instanceof ConfigScreen) {
                        WidgetDrawer.removeOffset(parent);
                    }
                    mc.setScreen(parent);
                },
                MENU_CLICK
        );

        Button settingsButton = new Button(
                this.width - 20 - mc.textRenderer.getWidth("Settings"),
                5,
                50,
                16,
                "Settings",
                List.of(),
                Radon.defaultTextures ? 0xffffffff : 0,
                (button) -> mc.execute(() -> mc.setScreen(Radon.settings.setParent(this).fromTop())),
                MENU_CLICK
        );

        int offset = 0;

        for (EntrypointContainer<ConfigScreen> container : FabricLoader.getInstance().getEntrypointContainers("radon", ConfigScreen.class)) {

            ModContainer mod = container.getProvider();
            Optional<String> iconPathOpt = mod.getMetadata().getIconPath(19);

            Identifier iconId = Identifier.of("radon", "icons/" + mod.getMetadata().getId());

            if (iconPathOpt.isPresent()) {
                try {
                    InputStream stream = mod.getPath(iconPathOpt.get()).toUri().toURL().openStream();
                    NativeImage image = NativeImage.read(stream);
                    NativeImageBackedTexture texture = //? if <=1.21.4 {
                            /*new NativeImageBackedTexture(image);
                            *///? } else {
                            new NativeImageBackedTexture(iconId::toString, image);
                            //? }
                    mc.getTextureManager().registerTexture(iconId, texture);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            RectBox box = new RectBox(
                    50 + offset - 20,
                    50 - 4,
                    100 + offset,
                    50 + textRenderer.fontHeight + 2,
                    false,
                    true,
                    true,
                    false,
                    0x88000000,
                    0xffffffff,
                    List.of(),
                    false,
                    b -> mc.execute(() -> mc.setScreen(container.getEntrypoint().setParent(this)))
            );

            this.addDrawableChild(box);
            addSelectableChild(box);

            this.addDrawableChild(new TextWidget(
                    50 + offset,
                    50,
                    50,
                    mc.textRenderer.fontHeight,
                    Text.literal(mod.getMetadata().getName()).setStyle(Radon.fontStyle),
                    mc.textRenderer
            ));

            offset += 50;

        }

        String title = "Radon Menu";
        addDrawableChild(new TextWidget(
                (width - mc.textRenderer.getWidth(title)) / 2,
                10,
                mc.textRenderer.getWidth(title),
                mc.textRenderer.fontHeight,
                Text.literal(title).setStyle(Radon.fontStyle),
                mc.textRenderer
        ));

        this.addDrawableChild(backButton);
        this.addDrawableChild(settingsButton);

    }

    @Override
    public void resize(int width, int height) {

        CursorHelper.setCursor(CursorHelper.Cursors.NORMAL);

        super.resize(width, height);

    }

}
