package com.github.jenya705.database;

import com.github.jenya705.DiscomcConfig;
import com.github.jenya705.data.MinecraftConfigData;
import com.github.jenya705.data.SerializedData;
import lombok.Data;

@Data
public class DatabaseConfig implements DiscomcConfig {

    private String sqlType = "sqlite";
    private String sqlHost = "localhost:3306";
    private String sqlUser = "root";
    private String sqlPassword = "mysql";
    private String sqlDatabase = "Minecraft";
    private String sqlDriver = "default";
    private boolean discordIDUnique = false;

    @Override
    public void load(SerializedData data) {
        setSqlType(data.getString("sqlType", getSqlType()));
        setSqlHost(data.getString("sqlHost", getSqlHost()));
        setSqlUser(data.getString("sqlUser", getSqlUser()));
        setSqlPassword(data.getString("sqlPassword", getSqlPassword()));
        setSqlDatabase(data.getString("sqlDatabase", getSqlDatabase()));
        setSqlDriver(data.getString("sqlDriver", getSqlDriver()));
        setDiscordIDUnique(data.getBoolean("discordIDUnique", isDiscordIDUnique()));
    }

    @Override
    public void save(SerializedData data) {
        data.setObject("sqlType", getSqlType());
        data.setObject("sqlHost", getSqlHost());
        data.setObject("sqlUser", getSqlUser());
        data.setObject("sqlPassword", getSqlPassword());
        data.setObject("sqlDatabase", getSqlDatabase());
        data.setObject("sqlDriver", getSqlDriver());
        data.setObject("discordIDUnique", isDiscordIDUnique());
    }
}
