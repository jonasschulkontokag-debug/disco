package de.example.quickdisconnect;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickDisconnectClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("quickdisconnect");
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("quickdisconnect", "main"));

    private static KeyMapping disconnectKey;
    private static PauseScreen pendingScreen = null;
    private static int attempts = 0;

    @Override
    public void onInitializeClient() {
        disconnectKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.quickdisconnect.disconnect",
                InputConstants.KEY_1,          // default key: 1
                CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Step 2: pause menu is open -> find the disconnect button, focus it, press Enter
            if (pendingScreen != null) {
                PauseScreen screen = pendingScreen;
                attempts++;
                Button button = findDisconnectButton(screen);
                if (button != null) {
                    pendingScreen = null;
                    screen.setFocused(button);
                    screen.keyPressed(new KeyEvent(InputConstants.KEY_RETURN, 0, 0));
                } else if (attempts >= 5) {
                    pendingScreen = null;
                    LOGGER.warn("Disconnect button not found. Buttons on the pause screen:");
                    for (GuiEventListener child : screen.children()) {
                        if (child instanceof Button b) {
                            LOGGER.warn(" - {} ({})", b.getMessage().getString(), b.getMessage().getContents());
                        }
                    }
                }
            }

            // Step 1: hotkey pressed -> open the pause menu (like Escape)
            while (disconnectKey.consumeClick()) {
                if (client.level != null && pendingScreen == null) {
                    PauseScreen screen = new PauseScreen(true);
                    client.setScreenAndShow(screen);   // 26.2+: renamed from setScreen
                    pendingScreen = screen;
                    attempts = 0;
                }
            }
        });
    }

    private static Button findDisconnectButton(PauseScreen screen) {
        for (GuiEventListener child : screen.children()) {
            if (child instanceof Button b && b.active
                    && b.getMessage().getContents() instanceof TranslatableContents t) {
                String k = t.getKey();
                if (k.equals("menu.disconnect") || k.equals("menu.returnToMenu")) {
                    return b;
                }
            }
        }
        return null;
    }
}
