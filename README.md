# LightsaberVR - Minecraft 1.20.4 Forge Mod

A Minecraft mod that adds lightsabers with full Vivecraft VR support!

## Features

- **10 Unique Lightsabers**: Red, Blue, Green, Purple, Yellow, Orange, Cyan, Magenta, White (Darksaber), and Black (Void Saber)
- **Full VR Support**: Integration with Vivecraft VR API for controller tracking
- **Haptic Feedback**: Feel your lightsaber impacts through VR controller rumble
- **Extended Reach in VR**: 2-block extended reach when playing in VR mode
- **Custom Rendering**: Glowing blade effects with color-coded particles
- **Unique Stats**: Each lightsaber has different damage and attack speed values
- **Fire Damage**: Lightsabers ignite enemies on hit!

## Installation

### Requirements
- Minecraft 1.20.4
- Forge 49.0.30 or higher
- [Vivecraft VR API](https://www.curseforge.com/minecraft/mc-mods/mc-vr-api) (optional, for VR features)

### Standard Mode
1. Install Forge for 1.20.4
2. Place `lightsabersvr-[version].jar` in your `mods` folder
3. Launch Minecraft and enjoy!

### VR Mode (Vivecraft)
1. Install Vivecraft for 1.20.4
2. Install the VR API mod
3. Place `lightsabersvr-[version].jar` in your `mods` folder
4. Launch in VR mode for full controller tracking and haptic feedback!

## Lightsaber Stats

| Color | Attack Damage | Attack Speed | Special |
|-------|--------------|--------------|---------|
| Red | 10.0 | 2.0 | Balanced Jedi/Sith weapon |
| Blue | 9.0 | 1.8 | Jedi Guardian style |
| Green | 8.5 | 1.7 | Jedi Consular style |
| Purple | 9.5 | 1.9 | Mace Windu style |
| Yellow | 8.0 | 1.6 | Temple Guard style |
| Orange | 9.0 | 1.75 | Custom saber color |
| Cyan | 8.5 | 1.65 | Custom saber color |
| Magenta | 10.5 | 2.1 | High damage variant |
| White | 12.0 | 2.5 | Darksaber - Very powerful! |
| Black | 15.0 | 3.0 | Void Saber - Ultimate power! |

## Controls

- **Right-click**: Toggle lightsaber on/off (with sound effects)
- **Attack**: Swing lightsaber to deal damage and ignite enemies
- **VR Mode**: Controllers automatically tracked with extended reach

## Development

### Building from Source

```bash
# Clone the repository
git clone https://github.com/TheOnl-Coder/mcvraddon.git
cd mcvraddon

# Build the mod
./gradlew build

# Find the output in build/libs/
```

### Project Structure

```
src/main/java/com/theonl_coder/lightsabersvr/
├── LightsaberVRMod.java          # Main mod class
├── VRAPIPluginImpl.java          # VRAPI plugin implementation
├── item/
│   ├── ModItems.java             # Item registry
│   └── LightsaberItem.java       # Lightsaber item class
├── vr/
│   └── VRIntegration.java        # VR API integration
└── client/renderer/
    └── LightsaberVRRenderer.java # VR rendering
```

## API Usage (for other mods)

This mod registers itself as a VRAPI plugin. Other mods can check for lightsabers:

```java
if (stack.getItem() instanceof LightsaberItem) {
    LightsaberItem saber = (LightsaberItem) stack.getItem();
    int color = saber.getBladeColor();
    // Do something with the lightsaber...
}
```

## License

MIT License - Feel free to use this code in your own projects!

## Credits

- **TheOnl-Coder** - Original author
- **Vivecraft Team** - For the amazing VR API
- **Star Wars** - For inspiring the lightsaber design

## Support

If you encounter any issues or have suggestions, please open an issue on GitHub!
