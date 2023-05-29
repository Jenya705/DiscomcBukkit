package com.github.jenya705.database;

import com.github.jenya705.Discomc;
import com.github.jenya705.DiscomcModule;
import com.github.jenya705.util.ConfigsUtil;
import com.github.jenya705.util.FilesUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

@Getter
@Setter(AccessLevel.PROTECTED)
public class DatabaseModule implements DiscomcModule {

    public static final String[] availableSQLTypes = new String[]{"mysql", "sqlite"};

    private Connection connection;
    private DatabaseConfig config;
    private DatabaseConnector connector;

    @Override
    public void onLoad() {
        setConfig(ConfigsUtil.loadConfig(new DatabaseConfig(), "database"));
        Discomc discomc = Discomc.getPlugin();
        try {
            cloneSqlScripts("insert", "select_all", "select_by_discord_id",
                    "select_by_registration_id", "select_by_uuid",
                    "select_by_uuid_and_discord_id", "setup_all_unique",
                    "setup_discord_id_unique", "setup_no_unique", "setup_uuid_unique");
        } catch (IOException e) {
            discomc.getLogger().log(Level.SEVERE, "[Database] Can not clone sql scripts " +
                    "because of an exception, disabling plugin:", e);
            discomc.getServer().getPluginManager().disablePlugin(discomc);
        }
        if (getConfig().getSqlDriver().equalsIgnoreCase("default")) {
            try {
                loadDriverDefault(getConfig().getSqlType());
            } catch (ClassNotFoundException | IllegalArgumentException e) {
                if (e instanceof ClassNotFoundException) {
                    discomc.getLogger().severe(String.format("[Database] Sql type %s is not loaded in plugin," +
                            " reinstall plugin and report this bug to developer, disabling plugin", getConfig().getSqlType()));
                } else {
                    discomc.getLogger().severe(String.format("[Database] Sql type %s is not exist" +
                            " in default context, disabling plugin", getConfig().getSqlType()));
                }
                discomc.getServer().getPluginManager().disablePlugin(discomc);
                return;
            }
        } else {
            try {
                Class.forName(getConfig().getSqlDriver());
            } catch (ClassNotFoundException e) {
                discomc.getLogger().severe("[Database] Given sql driver is not found, " +
                        "if you are using default sql type write default in field sqlDriver, " +
                        "if it custom sqlType you need to integrate jdbc of it, disabling plugin");
                discomc.getServer().getPluginManager().disablePlugin(discomc);
                return;
            }
        }
        try {
            if (getConfig().getSqlType().equalsIgnoreCase("sqlite")) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + new File(
                        discomc.getDataFolder(), "database.db").getAbsolutePath());
            } else {
                connection = DriverManager.getConnection(String.format("jdbc:%s://%s/%s",
                        getConfig().getSqlType().toLowerCase(Locale.ROOT), getConfig().getSqlHost(),
                        getConfig().getSqlDatabase()), getConfig().getSqlUser(), getConfig().getSqlPassword());
            }
        } catch (SQLException e) {
            discomc.getLogger().log(Level.SEVERE, "[Database] SQL exception, disabling plugin:", e);
            discomc.getServer().getPluginManager().disablePlugin(discomc);
            return;
        }
        setConnector(new ScriptedDatabaseConnector(getConfig().getSqlType()));
        getConnector().setup();
    }

    @Override
    public void onEnable() {
        /* NOTHING */
    }

    @Override
    public void onDisable() {
        try {
            if (getConnection() != null) {
                getConnection().close();
            }
        } catch (SQLException e) {
            Discomc.getPlugin().getLogger().log(Level.WARNING, "[Database] Can not close connection:", e);
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void disable() {
        Discomc discomc = Discomc.getPlugin();
        discomc.getLogger().severe("[Database] Database module disabled, disabling plugin");
        discomc.getServer().getPluginManager().disablePlugin(discomc);
    }

    public void loadDriverDefault(String sqlType) throws ClassNotFoundException {
        if (sqlType.equalsIgnoreCase("sqlite")) {
            Class.forName("org.sqlite.JDBC");
        } else if (sqlType.equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } else {
            throw new IllegalArgumentException(String.format(
                    "SQL type %s is not exist in default context", sqlType));
        }
    }

    public ResultSet query(String sql, Object... objects) throws SQLException {
        if (objects.length == 0) {
            return getConnection().createStatement().executeQuery(sql);
        } else {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; ++i) {
                statement.setObject(i + 1, objects[i]);
            }
            return statement.executeQuery();
        }
    }

    public ResultSet query(String sql, List<Object> objects) throws SQLException {
        if (objects.isEmpty()) {
            return getConnection().createStatement().executeQuery(sql);
        } else {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.size(); ++i) {
                statement.setObject(i + 1, objects.get(i));
            }
            return statement.executeQuery();
        }
    }

    public void update(String sql, Object... objects) throws SQLException {
        if (objects.length == 0) {
            getConnection().createStatement().executeUpdate(sql);
        } else {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; ++i) {
                statement.setObject(i + 1, objects[i]);
            }
            statement.executeUpdate();
        }
    }

    public void update(String sql, List<Object> objects) throws SQLException {
        if (objects.isEmpty()) {
            getConnection().createStatement().executeUpdate(sql);
        } else {
            PreparedStatement statement = getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.size(); ++i) {
                statement.setObject(i + 1, objects.get(i));
            }
            statement.executeUpdate();
        }
    }

    private void cloneSqlScripts(String... scripts) throws IOException {
        Discomc discomc = Discomc.getPlugin();
        File sqlDirectoryAsFile = new File(discomc.getDataFolder(), "sql");
        sqlDirectoryAsFile.mkdir();
        for (String script : scripts) {
            InputStream defaultInputStream = getClass().getClassLoader().getResourceAsStream("sql/" + script + ".sql");
            if (defaultInputStream != null) {
                File file = FilesUtil.getRecursivelyFile("sql", script + ".sql");
                if (!file.exists()) {
                    file.createNewFile();
                    FilesUtil.saveFromInputStream(file, defaultInputStream);
                }
            }
            for (String sqlDirectory : availableSQLTypes) {
                FilesUtil.getRecursivelyFile("sql", sqlDirectory).mkdir();
                InputStream fixedInputStream = getClass().getClassLoader().getResourceAsStream("sql/" + sqlDirectory + "/" + script + ".sql");
                if (fixedInputStream != null) {
                    File fixedFile = FilesUtil.getRecursivelyFile("sql", sqlDirectory, script + ".sql");
                    if (!fixedFile.exists()) {
                        fixedFile.createNewFile();
                        FilesUtil.saveFromInputStream(fixedFile, fixedInputStream);
                    }
                }
            }
        }
    }

}
