package space.cubicworld;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DiscomcRoles {

    private Map<Long, String> roles = new HashMap<>();

    public DiscomcRoles() throws IOException, ParseException {
        File rolesFile = new File(DiscomcPlugin.getInstance().getDataFolder(), "roles.json");
        if (rolesFile.exists()){
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(rolesFile));
            Set entrySet = jsonObject.entrySet();
            for (Object entryObject: entrySet){
                Entry<Object, Object> entry = (Entry) entryObject;
                Object keyObject = entry.getKey();
                Object valueObject = entry.getValue();
                if (!(keyObject instanceof String) || !(valueObject instanceof Long)){
                    DiscomcPlugin.getInstance().getLogger().warning("Discomc roles loading failed because values in file not in types which required");
                    return;
                }
                String key = (String) keyObject;
                Long value = (Long) valueObject;
                roles.put(value, key);
            }
        }
        else {
            rolesFile.createNewFile();
            Files.write(rolesFile.toPath(), "{}".getBytes(), StandardOpenOption.WRITE);
        }
    }

    public String getRole(Long id){
        return roles.getOrDefault(id, null);
    }

}
