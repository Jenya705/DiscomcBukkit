package space.cubicworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.apache.commons.lang.text.StrSubstitutor;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import space.cubicworld.command.DiscomcCommand;
import space.cubicworld.connect.ConnectModule;
import space.cubicworld.console.ConsoleModule;
import space.cubicworld.database.DatabaseModule;
import space.cubicworld.discord.DiscordModule;
import space.cubicworld.multichat.MultiChatModule;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class DiscomcPlugin extends JavaPlugin {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final Random random = new Random();

    public static final int PLUGIN_METRICS_ID = 10416;

    @Getter
    private static DiscomcPlugin discomcPlugin;

    private DiscomcSave discomcSave;
    private DiscomcMessages discomcMessages;
    private DiscomcUtil discomcUtil;

    private DiscomcCommand discomcCommand;

    private MultiChatModule multiChatModule;
    private DiscordModule discordModule;
    private DatabaseModule databaseModule;
    private ConnectModule connectModule;
    private ConsoleModule consoleModule;

    public DiscomcPlugin() throws InvalidPluginException {
        if (discomcPlugin != null) throw new InvalidPluginException("Plugin constructor called twice!");
        discomcPlugin = this;
    }

    public void loadSettings(){
        try {
            //createConfigIfNotExist();
            createSaveIfNotExist();
        }
        catch (IOException e) {
            getLogger().log(Level.SEVERE, "Can not create file, disable plugin:", e);
            setEnabled(false);
        }

        discomcMessages = new DiscomcMessages(new File(getDataFolder(), "messages.properties"));
        discomcSave = DiscomcSave.of(new File(getDataFolder(), "save.json"));
        if (discomcSave != null) discomcSave.save(new File(getDataFolder(), "save.json"));

    }

    public void saveConfig(File config) throws IOException {

        if (!config.exists()) config.createNewFile();

        InputStream resourceConfig = getClassLoader().getResourceAsStream("config-template.yml");
        if (resourceConfig == null) return;
        byte[] resourceConfigBytes = new byte[resourceConfig.available()];
        resourceConfig.read(resourceConfigBytes);
        String defaultConfig = new String(resourceConfigBytes);

        Map<String, String> placeholders = new HashMap<>();

        discordModule.getConfig().save(placeholders);
        databaseModule.getConfig().save(placeholders);
        multiChatModule.getConfig().save(placeholders);
        consoleModule.getConfig().save(placeholders);
        connectModule.getConfig().save(placeholders);
        discomcUtil.getConfig().save(placeholders);

        String endConfig = StrSubstitutor.replace(defaultConfig, placeholders);

        Files.write(config.toPath(), endConfig.getBytes(), StandardOpenOption.WRITE);

    }

    @Override
    public void onLoad() {

        try {
            File configFile = new File(getDataFolder(), "config.yml");
            if (configFile.exists()) getConfig().load(configFile);
            else getConfig().loadFromString("");
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Can not load config:", e);
        }

        loadSettings();

        // vanilla modules start
        discordModule = new DiscordModule();
        databaseModule = new DatabaseModule();
        multiChatModule = new MultiChatModule();
        connectModule = new ConnectModule();
        consoleModule = new ConsoleModule();
        discomcUtil = new DiscomcUtil();

        ModuleStore.putModule("database", databaseModule);
        ModuleStore.putModule("discordAPI", discordModule);
        ModuleStore.putModule("multiChat", multiChatModule);
        ModuleStore.putModule("connect", connectModule);
        ModuleStore.putModule("console", consoleModule);
        // vanilla modules end

        ModuleStore.getModules().forEach((name, module) -> {
            try {
                getLogger().info(String.format("Loading %s module", name));
                module.load();
            } catch (Exception e){
                getLogger().log(Level.WARNING, String.format("Disable %s module because of exception:", name), e);
                module.setEnabled(false);
            }
        });

        try {
            saveConfig(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Can not save config:", e);
        }
    }

    @Override
    public void onEnable() {

        // enabling discomc utils
        discomcUtil.enable();

        try {
            Metrics metrics = new Metrics(this, PLUGIN_METRICS_ID);
            metrics.addCustomChart(new SimplePie("sqlType", () -> databaseModule.getConfig().getSqlType().toLowerCase()));
        } catch (Exception e){
            logger().log(Level.WARNING, "Metrics disabled because of the exception, report to developer: ", e);
        }

        discomcCommand = new DiscomcCommand();
        ModuleStore.getModules().forEach((name, module) -> {
            if (!module.isEnabled()) return;
            getLogger().info(String.format("Enabling %s module", name));
            module.enable();
        });
    }

    @Override
    public void onDisable() {
        getDiscomcSave().save(new File(getDataFolder(), "save.json"));
        ModuleStore.getModules().forEach((name, module) -> {
            if (!module.isEnabled()) return;
            getLogger().info(String.format("Disabling %s module", name));
            module.disable();
        });
    }

    @SuppressWarnings("Maybe unused")
    public void createConfigIfNotExist() throws IOException {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) return;
        saveDefaultConfig();
    }

    @SuppressWarnings("Maybe unused")
    public void writeConfigInFile(File file) throws IOException{
        InputStream resourceConfig = getClassLoader().getResourceAsStream("config-template.yml");
        byte[] resourceConfigBytes = new byte[resourceConfig.available()];
        resourceConfig.read(resourceConfigBytes);
        FileWriter configFileInputStream = new FileWriter(file);
        configFileInputStream.write(new String(resourceConfigBytes));
        configFileInputStream.flush();
    }

    public void createSaveIfNotExist() throws IOException {
        File saveFile = new File(getDataFolder(), "save.json");
        if (saveFile.exists()) return;
        Files.write(saveFile.toPath(), "{}".getBytes());
    }

    public static void pluginEnabled(boolean enabled){
        if (!enabled) logger().warning("Discomc plugin is disabled!");
        getDiscomcPlugin().setEnabled(enabled);
    }

    public static Logger logger(){
        return getDiscomcPlugin().getLogger();
    }

}
