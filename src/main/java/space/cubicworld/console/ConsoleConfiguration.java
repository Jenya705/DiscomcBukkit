package space.cubicworld.console;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class ConsoleConfiguration {

    private int logEventsSendDelay = 40;

    public ConsoleConfiguration(FileConfiguration fileConfiguration){
        setLogEventsSendDelay(fileConfiguration.getInt("console.logEventsSendDelay"));
    }

}
