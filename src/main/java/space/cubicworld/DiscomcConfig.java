package space.cubicworld;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public interface DiscomcConfig {

    void load(FileConfiguration config);

    void save(Map<String, String> placeholders);

    static void put(Map<String, String> placeholders, String key, int value){
        placeholders.put(key, Integer.toString(value));
    }

    static void put(Map<String, String> placeholders, String key, long value){
        placeholders.put(key, Long.toString(value));
    }

    static void put(Map<String, String> placeholders, String key, String value){
        placeholders.put(key, String.format("'%s'", value));
    }

    static void put(Map<String, String> placeholders, String key, short value){
        placeholders.put(key, Short.toString(value));
    }

    static void put(Map<String, String> placeholders, String key, byte value){
        placeholders.put(key, Byte.toString(value));
    }

    static void put(Map<String, String> placeholders, String key, boolean value){
        placeholders.put(key, Boolean.toString(value));
    }

}
