package space.cubicworld;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import space.cubicworld.command.DiscomcConnectCommand;
import space.cubicworld.database.DiscomcDatabase;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

@Getter
@Setter
public class DiscomcPlugin extends JavaPlugin {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static DiscomcPlugin instance = null;

    private SettingsManager discomcConfig;
    private SettingsManager discomcSave;
    private DiscomcMessages discomcMessages;

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
            setDiscomcMessages(DiscomcMessages.load(new File(getDataFolder(), "messages.json")));
            setDiscomcDatabase(new DiscomcDatabase());
            setDiscordManager(new DiscordManager());
        } catch (Exception e){
            getLogger().log(Level.SEVERE, "Error while enabling plugin:", e);
            setEnabled(false);
        }

        getCommand("connect").setExecutor(new DiscomcConnectCommand());

    }

    @Override
    public void onDisable() {

        getDiscordManager().getJda().shutdownNow();

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

}
