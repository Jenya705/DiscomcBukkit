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
import org.json.simple.parser.ParseException;
import com.github.jenya705.command.ConnectCommand;
import com.github.jenya705.command.DiscomcCommand;
import space.cubicworld.console.DiscomcConsoleAppender;
import space.cubicworld.multichat.MinecraftChatHandler;

import java.io.File;
import java.io.IOException;
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
    private DiscomcRoles discomcRoles;

    private MojangApi mojangApi;
    private DiscomcDatabase discomcDatabase;
    private DiscordManager discordManager;

    public DiscomcPlugin() throws IllegalAccessException{
        if (instance == null) {
            instance = this;
        }
        else{
            throw new IllegalAccessException("Plugin created twice, report to developer");
        }
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

        getCommand("discomc").setExecutor(new DiscomcCommand());
        loadAll();

    }

    @Override
    public void onDisable() {

        finishAll();
        getDiscomcSave().save();

    }

    public void finishAll(){
        getBukkitTasks().forEach(BukkitTask::cancel);
        getDiscordManager().getJda().shutdown();

        try {
            getDiscomcDatabase().getConnection().close();
        } catch (SQLException e){
            getLogger().log(Level.WARNING, "SQL Connection not closed", e);
        }
    }

    public void loadAll(){

        getDiscomcConfig().reload();
        getDiscomcSave().reload();

        setMojangApi(new MojangApi());

        try {
            setDiscomcMessages(new DiscomcMessages());
            setDiscomcDatabase(new DiscomcDatabase());
            setDiscordManager(new DiscordManager());
        } catch (Exception e){
            getLogger().log(Level.SEVERE, "Error while enabling plugin:", e);
            setEnabled(false);
        }

        getCommand("connect").setExecutor(new ConnectCommand());

        if (getDiscomcConfig().getProperty(DiscomcConfiguration.MULTI_CHAT_ENABLED))
            getServer().getPluginManager().registerEvents(new MinecraftChatHandler(), this);

        if (getDiscomcConfig().getProperty(DiscomcConfiguration.CONSOLE_ENABLED)){
            Logger logger = (Logger) LogManager.getRootLogger();
            logger.addAppender(new DiscomcConsoleAppender("Discomc-Console"));
        }

        if (getDiscomcConfig().getProperty(DiscomcConfiguration.DISCORD_MINECRAFT_ROLE_LINK)){
            try {
                setDiscomcRoles(new DiscomcRoles());
            } catch (ParseException | IOException e) {
                getLogger().log(Level.SEVERE, "Can not load roles! Roles giving future disabled");
            }
        }

    }

    public static DiscomcPlugin getInstance(){
        return instance;
    }

    public static void addBukkitTask(BukkitTask bukkitTask){
        getInstance().bukkitTasks.add(bukkitTask);
    }

}
