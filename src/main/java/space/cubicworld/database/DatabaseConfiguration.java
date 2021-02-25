package space.cubicworld.database;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import space.cubicworld.DiscomcConfig;

import java.util.Map;

@Data
public class DatabaseConfiguration implements DiscomcConfig {

    private String sqlType = "sqlite";
    private String host = "localhost:6543";
    private String databaseName = "Minecraft";
    private String user = "postgres";
    private String password = "admin";

    public DatabaseConfiguration(FileConfiguration config){
        load(config);
    }

    @Override
    public void load(FileConfiguration config) {
        setSqlType(config.getString("database.sqlType", getSqlType()));
        setHost(config.getString("database.host", getHost()));
        setDatabaseName(config.getString("database.name", getDatabaseName()));
        setUser(config.getString("database.user", getUser()));
        setPassword(config.getString("database.password", getPassword()));
    }

    @Override
    public void save(Map<String, String> placeholders) {
        DiscomcConfig.put(placeholders, "database.sqlType", getSqlType());
        DiscomcConfig.put(placeholders, "database.host", getHost());
        DiscomcConfig.put(placeholders, "database.name", getDatabaseName());
        DiscomcConfig.put(placeholders, "database.user", getUser());
        DiscomcConfig.put(placeholders, "database.password", getPassword());
    }
}
