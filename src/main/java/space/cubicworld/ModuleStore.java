package space.cubicworld;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ModuleStore {

    @Getter
    private static final Map<String, DiscomcModule> modules = new HashMap<>();

    public static void putModule(String name, DiscomcModule module){
        modules.put(name, module);
    }

    public static DiscomcModule getModule(String name){
        return modules.getOrDefault(name, null);
    }

}
