package space.cubicworld.shortcut;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;

import java.util.Map;

@Data
public class ShortcutConfiguration implements DiscomcConfig {

    private boolean enabled = true;
    private String commandsPrefix = ">";
    private int messageDeleteTime = 5;
    private int responseDeleteTime = 20;
    private int commandDeleteTime = -1;

    public ShortcutConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setEnabled(config.getBoolean("shortcut.enabled", isEnabled()));
        setCommandsPrefix(config.getString("shortcut.commandsPrefix", getCommandsPrefix()));
        setMessageDeleteTime(config.getInt("shortcut.messageDeleteTime", getMessageDeleteTime()));
        setResponseDeleteTime(config.getInt("shortcut.responseDeleteTime", getResponseDeleteTime()));
        setCommandDeleteTime(config.getInt("shortcut.commandDeleteTime", getCommandDeleteTime()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "shortcut.enabled", isEnabled());
        DiscomcConfig.put(placeholders, "shortcut.commandsPrefix", getCommandsPrefix());
        DiscomcConfig.put(placeholders, "shortcut.messageDeleteTime", getMessageDeleteTime());
        DiscomcConfig.put(placeholders, "shortcut.responseDeleteTime", getResponseDeleteTime());
        DiscomcConfig.put(placeholders, "shortcut.commandDeleteTime", getCommandDeleteTime());
    }

}
