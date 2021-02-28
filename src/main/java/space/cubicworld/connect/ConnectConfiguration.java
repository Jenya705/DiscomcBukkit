package space.cubicworld.connect;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;

import java.util.Map;

@Data
public class ConnectConfiguration implements DiscomcConfig {

    private int codeRemovingTime = 6000;
    private int maxCodeValue = 10000;
    private int discordMessageDeleteTime = 5;
    private boolean enabled = true;

    // disconnect start
    private boolean disconnectEnabled = true;
    private boolean disconnectConfirm = true;
    private int disconnectTimeConfirm = 3000;
    // disconnect end

    public ConnectConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setCodeRemovingTime(config.getInt("connect.codeRemovingTime", getCodeRemovingTime()));
        setMaxCodeValue(config.getInt("connect.maxCodeValue", getMaxCodeValue()));
        setDiscordMessageDeleteTime(config.getInt("connect.discordMessageDeleteTime", getDiscordMessageDeleteTime()));
        setEnabled(config.getBoolean("connect.enabled", isEnabled()));

        // disconnect start
        setDisconnectEnabled(config.getBoolean("disconnect.enabled", isDisconnectEnabled()));
        setDisconnectConfirm(config.getBoolean("disconnect.confirm", isDisconnectConfirm()));
        setDisconnectTimeConfirm(config.getInt("disconnect.timeConfirm", getDisconnectTimeConfirm()));
        // disconnect end
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "connect.codeRemovingTime", getCodeRemovingTime());
        DiscomcConfig.put(placeholders, "connect.maxCodeValue", getMaxCodeValue());
        DiscomcConfig.put(placeholders, "connect.discordMessageDeleteTime", getDiscordMessageDeleteTime());
        DiscomcConfig.put(placeholders, "connect.enabled", isEnabled());

        // disconnect start
        DiscomcConfig.put(placeholders, "disconnect.enabled", isDisconnectEnabled());
        DiscomcConfig.put(placeholders, "disconnect.confirm", isDisconnectConfirm());
        DiscomcConfig.put(placeholders, "disconnect.timeConfirm", getDisconnectTimeConfirm());
        // disconnect end
    }
}
