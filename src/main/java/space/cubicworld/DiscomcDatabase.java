package space.cubicworld;

import ch.jalu.configme.SettingsManager;
import lombok.Getter;

import java.io.*;
import java.sql.*;
import java.util.UUID;

@Getter
public class DiscomcDatabase {

    private Connection connection;
    private String sqlType;

    public DiscomcDatabase() throws SQLException, IOException, ClassNotFoundException {
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
        SettingsManager discomcConfiguration = discomcPlugin.getDiscomcConfig();
        boolean sqlEnabled = discomcConfiguration.getProperty(DiscomcConfiguration.SQL_ENABLED);
        if (!sqlEnabled){
            sqlType = "sqlite";
            File liteSQLFile = new File(discomcPlugin.getDataFolder(), "database.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + liteSQLFile.getAbsolutePath());
        }
        else {
            sqlType = discomcConfiguration.getProperty(DiscomcConfiguration.SQL_TYPE);
            loadDriver(sqlType);
            connection = DriverManager.getConnection(String.format(
                    "jdbc:%s://%s:%s/%s",
                    discomcConfiguration.getProperty(DiscomcConfiguration.SQL_TYPE),
                    discomcConfiguration.getProperty(DiscomcConfiguration.SQL_HOST),
                    discomcConfiguration.getProperty(DiscomcConfiguration.SQL_PORT),
                    discomcConfiguration.getProperty(DiscomcConfiguration.SQL_DATABASE)
            ), discomcConfiguration.getProperty(DiscomcConfiguration.SQL_USER), discomcConfiguration.getProperty(DiscomcConfiguration.SQL_PASSWORD));
        }
        applyScript("setup.sql", true);
    }

    protected void loadDriver(String type) throws ClassNotFoundException {

        switch (type){
            case "postgresql":
                Class.forName("org.postgresql.Driver");
                break;
            case "mysql":
                Class.forName("com.mysql.jdbc.Driver");
                break;
        }

    }

    public ResultSet query(String sql, Object... objects) throws SQLException{
        if (objects.length == 0){
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        }
        else {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; ++i){
                preparedStatement.setObject(i+1, objects[i]);
            }
            return preparedStatement.executeQuery();
        }
    }

    public void update(String sql, Object... objects) throws SQLException {
        if (objects.length == 0){
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        else {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; ++i){
                preparedStatement.setObject(i+1, objects[i]);
            }
            preparedStatement.executeUpdate();
        }
    }

    public void applyScript(String fileName, boolean resource) throws IOException, SQLException {
        if (resource){
            InputStream fixedInputStream = getClass().getClassLoader().getResourceAsStream(getSqlType() + "/" + fileName);
            if (fixedInputStream != null){
                applyScript(fixedInputStream);
            }
            else {
                InputStream scriptInputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                if (scriptInputStream == null) throw new NullPointerException();
                applyScript(scriptInputStream);
            }
        }
        else {
            DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
            File fixedFile = new File(discomcPlugin.getDataFolder(), getSqlType() + "/" + fileName);
            if (fixedFile.exists()){
                applyScript(fixedFile);
            }
            else {
                File scriptFile = new File(discomcPlugin.getDataFolder(), fileName);
                if (!scriptFile.exists()) throw new NullPointerException();
                applyScript(scriptFile);
            }
        }
    }

    public void applyScript(File file) throws IOException, SQLException {
        applyScript(new FileInputStream(file));
    }

    public void applyScript(InputStream inputStream) throws IOException, SQLException {
        String sql = "";
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sql += line + "\n";
            }
        }
        update(sql);
    }

    public UUID getPlayerByDiscordID(long discordID) throws SQLException {
        ResultSet resultSet = query("SELECT * FROM dmc_players WHERE discordID = ?", discordID);
        boolean haveNext = resultSet.next();
        if (!haveNext) return null;
        return new UUID(resultSet.getLong("mostUUID"), resultSet.getLong("lessUUID"));
    }

}
