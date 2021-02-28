package space.cubicworld.database;

import lombok.Getter;
import space.cubicworld.DiscomcPlugin;

import java.io.*;
import java.util.logging.Level;

@Getter
public class DatabaseScriptStore {

    private String setupScript;
    private String playerInsertScript;
    private String playerSelectByUuidScript;
    private String playerSelectByDiscordIDScript;
    private String playerInsertWithCheckScript;
    private String playerDeleteByUuidScript;

    public DatabaseScriptStore(){
        loadScriptsFor(DiscomcPlugin.getDiscomcPlugin().getDatabaseModule().getConfig().getSqlType());
    }

    public void loadScriptsFor(String sqlType) {
        String loweredSqlType = sqlType.toLowerCase();
        try {
            setupScript = loadScript("setup.sql", loweredSqlType);
            playerInsertScript = loadScript("player_insert.sql", loweredSqlType);
            playerSelectByUuidScript = loadScript("player_select_uuid.sql", loweredSqlType);
            playerSelectByDiscordIDScript = loadScript("player_select_discordid.sql", loweredSqlType);
            playerInsertWithCheckScript = loadScript("player_insert_with_check.sql", loweredSqlType);
            playerDeleteByUuidScript = loadScript("player_delete_uuid.sql", loweredSqlType);
        } catch (IOException e){
            DiscomcPlugin.logger().log(Level.SEVERE, String.format("Can not load sql script for %s, disabling plugin:", loweredSqlType), e);
            DiscomcPlugin.pluginEnabled(false);
        }
    }

    private String loadScript(String scriptName, String sqlType) throws IOException {
        InputStream fixedScriptStream = getClass().getClassLoader().getResourceAsStream(String.format("sql/%s/%s", sqlType, scriptName));
        InputStream finalStream;
        if (fixedScriptStream == null){
            InputStream scriptStream = getClass().getClassLoader().getResourceAsStream(String.format("sql/%s", scriptName));
            if (scriptStream == null) return null;
            finalStream = scriptStream;
        }
        else {
            finalStream = fixedScriptStream;
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(finalStream));
        StringBuilder scriptBuilder = new StringBuilder();
        String buf;
        while ((buf = bufferedReader.readLine()) != null) scriptBuilder.append(buf).append("\n");
        return scriptBuilder.toString();
    }

}
