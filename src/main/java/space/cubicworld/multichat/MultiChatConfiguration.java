package space.cubicworld.multichat;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;
import space.cubicworld.DiscomcPlugin;

import java.util.Map;

@Data
public class MultiChatConfiguration implements DiscomcConfig {

    private String minecraftMessagePattern = "<{0}> {1}";
    private boolean enabled = true;
    private boolean vaultFeatures = true;

    public MultiChatConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setMinecraftMessagePattern(config.getString("multiChat.minecraftMessagePattern", getMinecraftMessagePattern()));
        setEnabled(config.getBoolean("multiChat.enabled", isEnabled()));
        setVaultFeatures(config.getBoolean("multiChat.vaultFeatures", isVaultFeatures()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "multiChat.minecraftMessagePattern", getMinecraftMessagePattern());
        DiscomcConfig.put(placeholders, "multiChat.enabled", isEnabled());
        DiscomcConfig.put(placeholders, "multiChat.vaultFeatures", isVaultFeatures());
    }
}
