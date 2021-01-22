package space.cubicworld;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import space.cubicworld.command.DiscomcConnectCommand;
import space.cubicworld.console.DiscomcAppender;
import space.cubicworld.database.DiscomcDatabase;
import space.cubicworld.handler.DiscomcChatMCHandler;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Getter
@Setter
public class DiscomcPlugin extends JavaPlugin {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static DiscomcPlugin instance = null;

    private List<BukkitTask> bukkitTasks = new ArrayList<>();

    private SettingsManager discomcConfig;
    private SettingsManager discomcSave;
    private DiscomcMessages discomcMessages;

    private DiscomcMojangApi mojangApi;
    private DiscomcDatabase discomcDatabase;
    private DiscordManager discordManager;

    public DiscomcPlugin(){
        if (instance == null) instance = this;
    }

    @Override
    public void onEnable() {

        setDiscomcConfig(SettingsManagerBuilder
                .withYamlFile(new File(getDataFolder(), "config.yml"))
                .configurationData(DiscomcConfiguration.class)
                .useDefaultMigrationService()
                .create()
        );

        setDiscomcSave(SettingsManagerBuilder
                .withYamlFile(new File(getDataFolder(), "saves/save.yml"))
                .configurationData(DiscomcSave.class)
                .useDefaultMigrationService()
                .create()
        );

        try {
            setDiscomcMessages(new DiscomcMessages());
            setDiscomcDatabase(new DiscomcDatabase());
            setDiscordManager(new DiscordManager());
        } catch (Exception e){
            getLogger().log(Level.SEVERE, "Error while enabling plugin:", e);
            setEnabled(false);
        }

        setMojangApi(new DiscomcMojangApi());

        getCommand("connect").setExecutor(new DiscomcConnectCommand());

        if (getDiscomcConfig().getProperty(DiscomcConfiguration.MULTI_CHAT_ENABLED))
            getServer().getPluginManager().registerEvents(new DiscomcChatMCHandler(), this);

        if (getDiscomcConfig().getProperty(DiscomcConfiguration.CONSOLE_ENABLED)){
            Logger logger = (Logger) LogManager.getRootLogger();
            logger.addAppender(new DiscomcAppender("Discomc-Console"));
        }

    }

    @Override
    public void onDisable() {

        getBukkitTasks().forEach(bt -> bt.cancel());
        getDiscordManager().getJda().shutdown();

        try {
            getDiscomcDatabase().getConnection().close();
        } catch (SQLException e){
            getLogger().log(Level.WARNING, "SQL Connection not closed", e);
        }

        getDiscomcConfig().save();
        getDiscomcSave().save();

    }

    public static DiscomcPlugin getInstance(){
        return instance;
    }

    public static void addBukkitTask(BukkitTask bukkitTask){
        getInstance().bukkitTasks.add(bukkitTask);
    }

}
