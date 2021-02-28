package space.cubicworld;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.JDA;
import org.bukkit.OfflinePlayer;
import space.cubicworld.command.DiscomcAdminCommand;
import space.cubicworld.connect.ConnectModule;
import space.cubicworld.database.DatabaseModule;
import space.cubicworld.discord.DiscordModule;

import java.util.UUID;

@UtilityClass
public class Discomc {

    /**
     * @param discordID
     * @return uuid or null if it is not registered in db or 0 uuid if an exception
     */
    public static UUID getUUID(long discordID){
        return ConnectModule.getUUID(discordID);
    }

    /**
     * @param uuid
     * @return discord id or null if it is not registered in db or -1 if it is an exception
     */
    public static Long getDiscordId(UUID uuid){
        return ConnectModule.getDiscordId(uuid);
    }

    /**
     * @param uuid
     * @return true if success
     */
    public static boolean deletePlayer(UUID uuid){
        return ConnectModule.deletePlayer(uuid);
    }

    /**
     * @param uuid
     * @param discordID
     * @return true if success
     */
    public static boolean upsertPlayer(UUID uuid, long discordID){
        return ConnectModule.upsertPlayer(uuid, discordID);
    }

    /**
     * @param uuid
     * @param discordID
     * @return true if success
     */
    public static boolean insertPlayer(UUID uuid, long discordID){
        return ConnectModule.insertPlayer(uuid, discordID);
    }

    /**
     *
     * @param discordID
     * @return null if player not registered and player uuid will be zero if it was sql exception
     */
    public static OfflinePlayer getPlayer(long discordID){
        return DiscomcUtil.getPlayer(discordID);
    }

    public static void addModule(String name, DiscomcModule discomcModule){
        ModuleStore.putModule(name, discomcModule);
    }

    /**
     * @param name
     * @return null if module is not exist
     */
    public static DiscomcModule getModule(String name){
        return ModuleStore.getModule(name);
    }

    public static DiscordModule getDiscordModule(){
        return DiscomcPlugin.getDiscomcPlugin().getDiscordModule();
    }

    public static JDA getJda(){
        return getDiscordModule().getJda();
    }

    public static DatabaseModule getDatabaseModule(){
        return DiscomcPlugin.getDiscomcPlugin().getDatabaseModule();
    }

    public static void addAdminCommand(String name, DiscomcAdminCommand command){
        DiscomcPlugin.getDiscomcPlugin().getDiscomcCommand().putCommand(name, command);
    }

    public static void addShortcut(String name, String content){
        DiscomcPlugin.getDiscomcPlugin().getShortcutModule().getShortcuts().put(name, content);
    }

}
