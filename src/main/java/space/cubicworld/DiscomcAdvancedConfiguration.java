package space.cubicworld;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

@Data
public class DiscomcAdvancedConfiguration implements DiscomcConfig{

    private int scheduleCommandsTimer = 20;

    public DiscomcAdvancedConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setScheduleCommandsTimer(config.getInt("advanced.scheduleCommandsTimer", getScheduleCommandsTimer()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "advanced.scheduleCommandsTimer", getScheduleCommandsTimer());
    }
}
