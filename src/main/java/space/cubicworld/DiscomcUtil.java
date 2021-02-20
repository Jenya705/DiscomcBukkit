package space.cubicworld;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
public class DiscomcUtil {

    private Deque<String> scheduledCommands;

    @Setter
    private int scheduleCommandsTimer = 20;

    public DiscomcUtil(){
        scheduledCommands = new ArrayDeque<>();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        FileConfiguration fileConfiguration = discomcPlugin.getConfig();
        setScheduleCommandsTimer(fileConfiguration.getInt("advanced.scheduleCommandsTimer"));
        discomcPlugin.getServer().getScheduler().runTaskTimer(discomcPlugin, () -> {
            while (scheduledCommands.peek() != null){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), scheduledCommands.pop());
            }
        }, getScheduleCommandsTimer(), getScheduleCommandsTimer());
    }

    public static void scheduleCommand(String command){
        DiscomcPlugin.getDiscomcPlugin().getDiscomcUtil()
                .scheduledCommands.add(command);
    }

}
