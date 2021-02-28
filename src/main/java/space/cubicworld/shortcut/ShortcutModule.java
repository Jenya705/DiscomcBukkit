package space.cubicworld.shortcut;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.command.DiscomcCommand;
import space.cubicworld.discord.DiscordModule;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class ShortcutModule implements DiscomcModule {

    @Setter
    private ShortcutConfiguration config;

    private Map<String, String> shortcuts;

    @Override
    public void load() {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        setConfig(new ShortcutConfiguration(discomcPlugin.getConfig()));
        if (!isEnabled()) return;
        shortcuts = new HashMap<>();
        try {
            File shortcutsFile = new File(discomcPlugin.getDataFolder(), "shortcuts.json");
            if (!shortcutsFile.exists()) {
                shortcutsFile.createNewFile();
                return;
            }
            shortcuts.put("shortcut", "shortcut");
            loadShortcuts(shortcutsFile);
        } catch (IOException | ParseException | ClassCastException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "Exception while loading shortcut module:", e);
        }
    }

    private void loadShortcuts(File shortcutsFile) throws IOException, ParseException, ClassCastException {
        JSONObject shortcutObjects = (JSONObject) new JSONParser().parse(new FileReader(shortcutsFile));
        shortcutObjects.forEach((key, value) -> {
            if (!(key instanceof String) || !(value instanceof String)) return;
            shortcuts.put(((String) key).toLowerCase(), (String) value);
        });
    }

    @Override
    public void enable() {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscordModule discordModule = discomcPlugin.getDiscordModule();
        DiscomcCommand discomcCommand = discomcPlugin.getDiscomcCommand();
        discordModule.getJda().addEventListener(new ShortcutDiscordHandler());
        discomcCommand.putCommand("shortcut", new ShortcutCommand());
    }

    @Override
    public void disable() {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        JSONObject shortcutObjects = new JSONObject(shortcuts);
        File shortcutsFile = new File(discomcPlugin.getDataFolder(), "shortcuts.json");
        try {
            if (!shortcutsFile.exists()) shortcutsFile.createNewFile();
            Files.write(shortcutsFile.toPath(), shortcutObjects.toJSONString().getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e){
            discomcPlugin.getLogger().log(Level.WARNING, "Can not save shortcuts because of an exception:", e);
        }
    }

    @Override
    public boolean isEnabled() {
        return getConfig().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getConfig().setEnabled(enabled);
    }

    @Override
    public String getDescription() {
        return "Shortcut minecraft commands to discord";
    }

}
