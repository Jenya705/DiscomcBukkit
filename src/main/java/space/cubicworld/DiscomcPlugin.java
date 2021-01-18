package space.cubicworld;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.logging.Level;

@Getter
@Setter
public class DiscomcPlugin extends JavaPlugin {

    private static DiscomcPlugin instance = null;

    private SettingsManager discomcConfig;
    private SettingsManager discomcSave;

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
            setDiscordManager(new DiscordManager());
        } catch (LoginException e){
            getLogger().log(Level.SEVERE, "Error while enabling plugin:", e);
            setEnabled(false);
        }

    }

    @Override
    public void onDisable() {

        getDiscomcConfig().save();
        getDiscomcSave().save();

    }

    public static DiscomcPlugin getInstance(){
        return instance;
    }

}
