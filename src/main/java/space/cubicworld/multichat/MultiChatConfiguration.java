package space.cubicworld.multichat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;

import java.util.Map;

@Data
public class MultiChatConfiguration implements DiscomcConfig {

    private String minecraftMessagePattern = "<{0}> {1}";
    private boolean enabled = true;

    public MultiChatConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setMinecraftMessagePattern(config.getString("multiChat.minecraftMessagePattern", getMinecraftMessagePattern()));
        setEnabled(config.getBoolean("multiChat.enabled", isEnabled()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "multiChat.minecraftMessagePattern", getMinecraftMessagePattern());
        DiscomcConfig.put(placeholders, "multiChat.enabled", isEnabled());
    }
}
