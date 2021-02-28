package space.cubicworld.connect;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.DiscomcSave;
import space.cubicworld.database.DatabaseModule;
import space.cubicworld.discord.DiscordModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ConnectModule implements DiscomcModule {

    public static final String CONNECT_COMMAND = "connect";
    public static final String DISCONNECT_COMMAND = "disconnect";

    @Getter
    private Map<Integer, Player> codes;

    @Getter
    @Setter
    private ConnectConfiguration config;

    @Override
    public void load() {
        FileConfiguration fileConfiguration = DiscomcPlugin.getDiscomcPlugin().getConfig();
        setConfig(new ConnectConfiguration(fileConfiguration));
    }

    @Override
    public void enable() {
        codes = new HashMap<>();
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        PluginCommand connectCommand = discomcPlugin.getCommand(CONNECT_COMMAND);
        if (connectCommand != null){
            connectCommand.setExecutor(new ConnectCommand());
        } else {
            discomcPlugin.getLogger().warning("Can not find connect command, disabling connect module!");
            setEnabled(false);
            return;
        }
        DiscordModule discordModule = discomcPlugin.getDiscordModule();
        DiscomcSave discomcSave = discomcPlugin.getDiscomcSave();
        if (discomcSave.getConnectChannelID() == 0) {
            discomcPlugin.getDiscomcSave().setConnectChannelID(
                    discordModule.createTextChannel("discomc-connect").getIdLong());
        }
        discordModule.getJda().addEventListener(new ConnectDiscordHandler());

        if (getConfig().isDisconnectEnabled()){
            PluginCommand disconnectCommand = discomcPlugin.getCommand(DISCONNECT_COMMAND);
            if (disconnectCommand == null){
                discomcPlugin.getLogger().log(Level.SEVERE, "Disconnect command disabled because command is not registered");
            }
            else {
                disconnectCommand.setExecutor(new DisconnectCommand());
            }
        }
    }

    @Override
    public void disable() {
        /* NOTHING */
    }

    /**
     * @param discordID
     * @return uuid or null if it is not registered in db or 0 uuid if an exception
     */
    public static UUID getUUID(long discordID){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        try {
            ResultSet result = databaseModule.query(databaseModule.getScriptStore().getPlayerSelectByDiscordIDScript(), discordID);
            if (result.next()){
                return new UUID(result.getLong("uuidMost"), result.getLong("uuidLeast"));
            }
        } catch (SQLException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "SQLException while getting uuid:", e);
            return new UUID(0, 0);
        }
        return null;
    }

    /**
     * @param uuid
     * @return discord id or null if it is not registered in db or -1 if it is an exception
     */
    public static Long getDiscordId(UUID uuid){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        try {
            ResultSet result = databaseModule.query(databaseModule.getScriptStore().getPlayerSelectByUuidScript(),
                    uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
            if (result.next()){
                return result.getLong("discordid");
            }
        } catch (SQLException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "SQLException while getting discord id:", e);
            return -1L;
        }
        return null;
    }

    /**
     * @param uuid
     * @return true if success
     */
    public static boolean deletePlayer(UUID uuid){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        try {
            databaseModule.update(databaseModule.getScriptStore().getPlayerDeleteByUuidScript(),
                    uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
        } catch (SQLException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "SQLException while trying to delete player:", e);
            return false;
        }
        return true;
    }

    /**
     * @param uuid
     * @param discordID
     * @return true if success
     */
    public static boolean upsertPlayer(UUID uuid, long discordID){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        try {
            databaseModule.update(databaseModule.getScriptStore().getPlayerInsertWithCheckScript(),
                    uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), discordID);
        } catch (SQLException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "SQLException while trying to upsert player:", e);
            return false;
        }
        return true;
    }

    /**
     * @param uuid
     * @param discordID
     * @return true if success
     */
    public static boolean insertPlayer(UUID uuid, long discordID){
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DatabaseModule databaseModule = discomcPlugin.getDatabaseModule();
        try {
            databaseModule.update(databaseModule.getScriptStore().getPlayerInsertScript(),
                    uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), discordID);
        } catch (SQLException e){
            discomcPlugin.getLogger().log(Level.SEVERE, "SQLException while trying to insert player:", e);
            return false;
        }
        return true;
    }


    @Override
    public boolean isEnabled() {
        return getConfig().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getConfig().setEnabled(enabled);
    }

    @Override
    public String getDescription() {
        return "Module which link minecraft and discord accounts";
    }



}
