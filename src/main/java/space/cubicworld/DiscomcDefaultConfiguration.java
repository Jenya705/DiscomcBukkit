package space.cubicworld;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

@Data
public class DiscomcDefaultConfiguration implements DiscomcConfig {

    private boolean useVaultFeaturesForPermissions = true;

    public DiscomcDefaultConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setUseVaultFeaturesForPermissions(config.getBoolean("useVaultFeaturesForPermissions", isUseVaultFeaturesForPermissions()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "default.useVaultFeaturesForPermissions", isUseVaultFeaturesForPermissions());
    }
}
