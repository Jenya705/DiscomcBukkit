package space.cubicworld.database;

import lombok.Getter;
import space.cubicworld.DiscomcPlugin;

import java.io.*;
import java.util.logging.Level;

@Getter
public class DatabaseScriptStore {

    private String setupScript;

    public DatabaseScriptStore(){
        loadScriptsFor(DiscomcPlugin.getDiscomcPlugin().getDatabaseModule().getConfig().getSqlType());
    }

    public void loadScriptsFor(String sqlType) {
        sqlType = sqlType.toLowerCase();
        try {
            setupScript = loadScript("setup.sql", sqlType);
        } catch (IOException | NullPointerException e){
            DiscomcPlugin.logger().log(Level.SEVERE, String.format("Can not load sql script for %s:", sqlType), e);
        }
    }

    private String loadScript(String scriptName, String sqlType) throws IOException, NullPointerException {
        DiscomcPlugin.logger().info(String.format("Loading %s script", scriptName));
        InputStream fixedScriptStream = getClass().getClassLoader().getResourceAsStream(String.format("sql/%s/%s", sqlType, scriptName));
        InputStream finalStream;
        if (fixedScriptStream == null){
            InputStream scriptStream = getClass().getClassLoader().getResourceAsStream(String.format("sql/%s", scriptName));
            if (scriptStream == null) throw new NullPointerException("Script is not exist");
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
