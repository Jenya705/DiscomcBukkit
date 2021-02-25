package space.cubicworld.console;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;

import java.util.Map;

@Data
public class ConsoleConfiguration implements DiscomcConfig {

    private int logEventsSendDelay = 40;
    private boolean enabled = true;

    public ConsoleConfiguration(FileConfiguration fileConfiguration){
        load(fileConfiguration);
    }

    @Override
    public void load(FileConfiguration config) {
        setLogEventsSendDelay(config.getInt("console.logEventsSendDelay", getLogEventsSendDelay()));
        setEnabled(config.getBoolean("console.enabled", isEnabled()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "console.logEventsSendDelay", getLogEventsSendDelay());
        DiscomcConfig.put(placeholders, "console.enabled", isEnabled());
    }
}
