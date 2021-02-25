package space.cubicworld.database;

import lombok.Getter;
import lombok.Setter;
import space.cubicworld.DiscomcModule;
import space.cubicworld.DiscomcPlugin;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;

@Getter
public class DatabaseModule implements DiscomcModule {

    private Connection connection;
    private DatabaseScriptStore scriptStore;

    @Setter
    private DatabaseConfiguration config;

    @Override
    public void load() {
        try {
            setConfig(new DatabaseConfiguration(DiscomcPlugin.getDiscomcPlugin().getConfig()));
            loadDriver();
            if (getConfig().getSqlType().equalsIgnoreCase("sqlite")){
                connection = DriverManager.getConnection("jdbc:sqlite:" +
                        new File(DiscomcPlugin.getDiscomcPlugin().getDataFolder(), "database.db"));
            }
            else {
                connection = DriverManager.getConnection(String.format("jdbc:%s://%s/%s",
                        getConfig().getSqlType().toLowerCase(), getConfig().getHost(), getConfig().getDatabaseName()),
                        getConfig().getUser(), getConfig().getPassword());
            }
            scriptStore = new DatabaseScriptStore();
            update(getScriptStore().getSetupScript());
        } catch(ClassNotFoundException | SQLException e){
            DiscomcPlugin.logger().log(Level.SEVERE, "Can not load database module because of exception:", e);
            DiscomcPlugin.pluginEnabled(false);
        }
    }

    private void loadDriver() throws ClassNotFoundException{
        String sqlType = getConfig().getSqlType();
        if (sqlType.equalsIgnoreCase("sqlite")){
            Class.forName("org.sqlite.jdbc4.JDBC4Connection");
        } else if (sqlType.equalsIgnoreCase("postgresql")) {
            Class.forName("org.postgresql.Driver");
        } else if (sqlType.equalsIgnoreCase("mysql")){
            Class.forName("com.mysql.jdbc.Driver");
        } else {
            DiscomcPlugin.logger().severe(String.format("%s is not supported in this discomc version! Disabling plugin!", sqlType));
            DiscomcPlugin.pluginEnabled(false);
        }
    }

    public ResultSet query(String sql, Object... objects) throws SQLException{
        if (objects.length == 0){
            return connection.createStatement().executeQuery(sql);
        }
        else {
            PreparedStatement preparedStatement = connection.prepareStatement(sql );
            for (int i = 0; i < objects.length; ++i) preparedStatement.setObject(i + 1, objects[i]);
            return preparedStatement.executeQuery();
        }
    }

    public void update(String sql, Object... objects) throws SQLException{
        if (objects.length == 0){
            connection.createStatement().executeUpdate(sql);
        }
        else {
            PreparedStatement preparedStatement = connection.prepareStatement(sql );
            for (int i = 0; i < objects.length; ++i) preparedStatement.setObject(i + 1, objects[i]);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void enable() {
        /* NOTHING */
    }

    @Override
    public void disable() {
        try {
            if (getConnection() != null) connection.close();
        } catch (SQLException e){
            DiscomcPlugin.logger().warning("Can not close sql connection");
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        /* NOTHING */
    }

    @Override
    public String getDescription() {
        return "Database module";
    }
}
