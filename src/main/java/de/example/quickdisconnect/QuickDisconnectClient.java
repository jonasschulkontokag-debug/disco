package de.example.quickdisconnect;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableTextContent;
import org.lwjgl.glfw.GLFW;

public class QuickDisconnectClient implements ClientModInitializer {
    private static KeyBinding disconnectKey;
    private static boolean pending = false;

    @Override
    public void onInitializeClient() {
        disconnectKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.quickdisconnect.disconnect",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_1,
                "category.quickdisconnect"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // 1) Taste gedrückt -> Pausenmenü öffnen (wie Escape)
            while (disconnectKey.wasPressed()) {
                if (client.world != null && !pending) {
                    client.setScreen(new GameMenuScreen(true));
                    pending = true;
                }
            }

            // 2) Menü ist offen -> mit Tab durchschalten, bis der Disconnect-Button fokussiert ist
            if (pending && client.currentScreen instanceof GameMenuScreen screen) {
                pending = false;
                for (int i = 0; i < 40; i++) {
                    screen.keyPressed(GLFW.GLFW_KEY_TAB, 0, 0);
                    if (isDisconnectButton(screen)) {
                        // 3) Enter drücken -> "Save and Quit" / "Disconnect"
                        screen.keyPressed(GLFW.GLFW_KEY_ENTER, 0, 0);
                        break;
                    }
                }
            }
        });
    }

    private static boolean isDisconnectButton(Screen screen) {
        if (screen.getFocused() instanceof ButtonWidget b
                && b.getMessage().getContent() instanceof TranslatableTextContent t) {
            String k = t.getKey();
            return k.equals("menu.disconnect") || k.equals("menu.returnToMenu");
        }
        return false;
    }
}
