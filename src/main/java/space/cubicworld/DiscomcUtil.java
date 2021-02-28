package space.cubicworld;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.shanerx.mojang.Mojang;
import space.cubicworld.connect.ConnectModule;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
public class DiscomcUtil {

    private Deque<Pair<CommandSender, String>> scheduledCommands;
    private Map<Long, OfflinePlayer> cachedPlayers;

    private final Mojang mojangApi;

    @Setter
    private DiscomcAdvancedConfiguration config;

    public DiscomcUtil(){
        cachedPlayers = new HashMap<>();
        mojangApi = new Mojang().connect();
        scheduledCommands = new ArrayDeque<>();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        setConfig(new DiscomcAdvancedConfiguration(discomcPlugin.getConfig()));
    }

    public void enable(){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        discomcPlugin.getServer().getScheduler().runTaskTimer(discomcPlugin, () -> {
            while (scheduledCommands.peek() != null){
                Pair<CommandSender, String> scheduledCommand = scheduledCommands.pop();
                Bukkit.dispatchCommand(scheduledCommand.getKey(), scheduledCommand.getValue());
            }
        }, getConfig().getScheduleCommandsTimer(), getConfig().getScheduleCommandsTimer());
        discomcPlugin.getServer().getScheduler().runTaskTimerAsynchronously(discomcPlugin, () -> cachedPlayers.clear(),
                getConfig().getCachedPlayersClearTimer(), getConfig().getCachedPlayersClearTimer());
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
        scheduleCommand(Bukkit.getConsoleSender(), command);
    }

    public static void scheduleCommand(CommandSender commandSender, String command){
        DiscomcPlugin.getDiscomcPlugin().getDiscomcUtil()
                .scheduledCommands.add(new Pair<>(commandSender, command));
    }

    public static OfflinePlayer getPlayer(long discordID){
        DiscomcUtil discomcUtil = DiscomcPlugin.getDiscomcPlugin().getDiscomcUtil();
        OfflinePlayer player = discomcUtil.getCachedPlayers().getOrDefault(discordID, null);
        if (player == null){
            ConnectModule connectModule = DiscomcPlugin.getDiscomcPlugin().getConnectModule();
            UUID playerUUID = connectModule.getUUID(discordID);
            if (playerUUID != null){
                player = Bukkit.getOfflinePlayer(playerUUID);
                discomcUtil.getCachedPlayers().put(discordID, player);
            }
            else {
                return null;
            }
        }
        return player;
    }

}
