# Quick Disconnect

A small client-side Fabric mod for Minecraft 1.21.1. Press a hotkey (default: `1`) and the mod
opens the pause menu, tabs to the **Disconnect / Save and Quit** button and presses it for you.

Eine kleine Client-Mod für Fabric: Taste drücken (Standard: `1`) → Pausenmenü öffnen →
per Tab zum Disconnect-Button → Enter. Fertig.

## Install
1. Install [Fabric Loader](https://fabricmc.net/use/) and [Fabric API](https://modrinth.com/mod/fabric-api).
2. Download the `.jar` (Actions → latest build → artifact, or Releases) and put it in `.minecraft/mods`.

## Build
Requires JDK 21 and Gradle:
```
gradle build
```
The jar ends up in `build/libs/`.

## Configure
Options → Controls → *Quick Disconnect*. Note that `1` is also the default hotbar slot 1 key.

## Note
The server only sees a normal disconnect. This mod does not hide anything from server rules
(e.g. combat-log punishments). Check the rules of the server you play on.

## License
MIT
