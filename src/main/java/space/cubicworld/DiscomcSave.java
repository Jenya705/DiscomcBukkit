package space.cubicworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.logging.Level;

@Data
public class DiscomcSave {

    public static final Gson saveGson = new GsonBuilder().setPrettyPrinting().create();

    private long categoryChannelID;
    private long multiChatChannelID;
    private long connectChannelID;
    private long consoleChannelID;
    private String multiChatWebhookURL = "";

    private DiscomcSave(){}

    public static DiscomcSave of(File saveFile){
        try {
            return saveGson.fromJson(new FileReader(saveFile), DiscomcSave.class);
        } catch(FileNotFoundException e){
            DiscomcPlugin.logger().log(Level.SEVERE, String.format("File not found %s", saveFile.getName()), e);
        }
        return null;
    }

    public void save(File saveFile) {
        try {
            Files.write(saveFile.toPath(), saveGson.toJson(this).getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e){
            DiscomcPlugin.logger().log(Level.SEVERE, "Can not save save file:", e);
        }
    }

}
