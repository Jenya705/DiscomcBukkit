package space.cubicworld.database;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class DatabaseConfiguration {

    private String sqlType;
    private String host;
    private String databaseName;
    private String user;
    private String password;

    public DatabaseConfiguration(FileConfiguration config){
        setSqlType(config.getString("database.sqlType"));
        setHost(config.getString("database.host"));
        setDatabaseName(config.getString("database.name"));
        setUser(config.getString("database.user"));
        setPassword(config.getString("database.password"));
    }
}
