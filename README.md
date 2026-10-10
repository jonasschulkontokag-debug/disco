# Quick Disconnect

A small client-side Fabric mod for **Minecraft 26.3**. Press a hotkey (default: `1`) and the mod
opens the pause menu, tabs to the **Disconnect / Save and Quit** button and presses it for you.

Kleine Client-Mod für Fabric (Minecraft 26.3): Taste drücken (Standard: `1`) → Pausenmenü öffnen →
per Tab zum Disconnect-Button → Enter.

## Install
1. Install [Fabric Loader](https://fabricmc.net/use/) (0.19.3+) for Minecraft 26.3 and [Fabric API](https://modrinth.com/mod/fabric-api).
2. Put the mod `.jar` into `.minecraft/mods`. Minecraft 26.x runs on Java 25.

## Build
Requires JDK 25 and Gradle 9.5+ (or generate a wrapper with `gradle wrapper`):
```
gradle build
```
The jar ends up in `build/libs/` (use the one without `-sources`).

## Configure
Options → Controls → *Quick Disconnect*. `1` is also the default hotbar slot 1 key.

## Notes
- Minecraft 26.x is unobfuscated; this project uses Mojang names and the `net.fabricmc.fabric-loom` plugin.
- The server only sees a normal disconnect. The mod does not hide anything from server rules
  (e.g. combat-log punishments).

## License
MIT
