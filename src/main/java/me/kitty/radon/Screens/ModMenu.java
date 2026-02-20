package me.kitty.radon.Screens;

import me.kitty.radon.Radon;
import me.kitty.radon.Widgets.*;
import me.kitty.radon.api.ConfigScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
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
            Optional<String> iconPathOpt = mod.getMetadata().getIconPath(16);

            if (iconPathOpt.isPresent()) {
                Identifier iconId = Identifier.of("radon", "icons/" + mod.getMetadata().getId());
                context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    iconId,
                    50 + offset - 20,
                    50 - 4,
                    0, 0,
                    18, 18,
                    16, 16
                );
            }

            offset += 50;
        }
    }


    @Override
    protected void init() {

        addDrawableChild(new StaticBox(-2, -2, width + 2, 30, 0x33000000,  0xffffffff, List.of()));

        Button backButton = new Button(
                10,
                5,
                50,
                16,
                parent != null ? "Back" : "Exit",
                List.of(),
                0,
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
                0,
                (button) -> mc.execute(() -> mc.setScreen(Radon.settings.setParent(this))),
                MENU_CLICK
        );

        Button conceptButton = new Button(
                20,
                height - 16 - 10,
                50,
                16,
                "Concept",
                List.of(),
                0,
                (button) -> mc.execute(() -> mc.setScreen(new ConceptScreen("SomeMod Settings", this))),
                MENU_CLICK
        );

        int offset = 0;

        for (EntrypointContainer<ConfigScreen> container : FabricLoader.getInstance().getEntrypointContainers("radon", ConfigScreen.class)) {

            ModContainer mod = container.getProvider();
            Optional<String> iconPathOpt = mod.getMetadata().getIconPath(18);

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

            this.addDrawableChild(new RectBox(
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
                    false
            ));

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

        String title = "Radon menu";
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
        this.addDrawableChild(conceptButton);

    }
}
