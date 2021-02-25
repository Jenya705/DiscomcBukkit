package space.cubicworld;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.shanerx.mojang.Mojang;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

@Getter
public class DiscomcUtil {

    private Deque<String> scheduledCommands;

    private final Mojang mojangApi;

    @Setter
    private DiscomcAdvancedConfiguration config;

    public DiscomcUtil(){
        mojangApi = new Mojang().connect();
        scheduledCommands = new ArrayDeque<>();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        setConfig(new DiscomcAdvancedConfiguration(discomcPlugin.getConfig()));
    }

    public void enable(){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        discomcPlugin.getServer().getScheduler().runTaskTimer(discomcPlugin, () -> {
            while (scheduledCommands.peek() != null){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), scheduledCommands.pop());
            }
        }, getConfig().getScheduleCommandsTimer(), getConfig().getScheduleCommandsTimer());
    }

    public static UUID getUuidPlayer(String playerNickname){
        if (Bukkit.getOnlineMode()){
            DiscomcUtil discomcUtil = DiscomcPlugin.getDiscomcPlugin().getDiscomcUtil();
            if (discomcUtil.getMojangApi().getStatus(Mojang.ServiceType.API_MOJANG_COM) == Mojang.ServiceStatus.GREEN) {
                try {
                    return UUID.fromString(discomcUtil.getMojangApi().getUUIDOfUsername(playerNickname).replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                    ));
                } catch (NullPointerException e) {
                    return null;
                }
            }
            else {
                return null;
            }
        }
        else {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerNickname).getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void scheduleCommand(String command){
        DiscomcPlugin.getDiscomcPlugin().getDiscomcUtil()
                .scheduledCommands.add(command);
    }

}
