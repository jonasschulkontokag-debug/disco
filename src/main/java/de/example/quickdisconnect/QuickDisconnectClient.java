package de.example.quickdisconnect;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;

public class QuickDisconnectClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("quickdisconnect", "main"));

    private static KeyMapping disconnectKey;
    private static PauseScreen pendingScreen = null;

    @Override
    public void onInitializeClient() {
        disconnectKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.quickdisconnect.disconnect",
                InputConstants.KEY_1,          // default key: 1
                CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Step 2 (next tick): pause menu is open -> Tab until the disconnect button has focus, then Enter
            if (pendingScreen != null) {
                PauseScreen screen = pendingScreen;
                pendingScreen = null;
                for (int i = 0; i < 40; i++) {
                    screen.keyPressed(new KeyEvent(InputConstants.KEY_TAB, 0, 0));
                    if (isDisconnectButton(screen)) {
                        screen.keyPressed(new KeyEvent(InputConstants.KEY_RETURN, 0, 0));
                        break;
                    }
                }
            }

            // Step 1: hotkey pressed -> open the pause menu (like Escape)
            while (disconnectKey.consumeClick()) {
                if (client.level != null && pendingScreen == null) {
                    PauseScreen screen = new PauseScreen(true);
                    client.setScreenAndShow(screen);   // 26.2+: renamed from setScreen
                    pendingScreen = screen;
                }
            }
        });
    }

    private static boolean isDisconnectButton(PauseScreen screen) {
        if (screen.getFocused() instanceof Button b
                && b.getMessage().getContents() instanceof TranslatableContents t) {
            String k = t.getKey();
            return k.equals("menu.disconnect") || k.equals("menu.returnToMenu");
        }
        return false;
    }
}
