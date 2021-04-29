package com.github.jenya705.data;

import com.github.jenya705.Discomc;
import com.github.jenya705.util.ConfigsUtil;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class YamlData extends SerializedMapData<String, Object> implements MinecraftConfigData {

    private static final Yaml yaml = new Yaml();

    @Override
    public void initialize(String data) {
        setData(yaml.load(data));
        if (getData() == null) {
            setData(new LinkedHashMap<>());
        }
    }

    @Override
    public Object getObject(Object key) {
        if (!(key instanceof String)) throw new IllegalArgumentException(
                "Key is not String, Yaml is not support this");
        // it can be exception because object is not exist
        return getData().get(key);
    }

    @Override
    public Object getObject(Object key, Object defaultValue) {

        if (!(key instanceof String)) throw new IllegalArgumentException(
                "Key is not String, Yaml is not support this");
        // this is protected from null pointer exception
        return getData().getOrDefault(key, defaultValue);

    }

    @Override
    public String toSerializedString() {
        return yaml.dumpAsMap(toNative());
    }

    public Map<String, Object> toNative(){
        Map<String, Object> nativeMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry: getData().entrySet()){
            Object obj = entry.getValue();
            if (obj instanceof YamlData){
                obj = ((YamlData) obj).toNative();
            }
            nativeMap.put(entry.getKey(), obj);
        }
        return nativeMap;
    }

    @Override
    public void setObject(Object key, Object value) {

        if (!(key instanceof String)) throw new IllegalArgumentException(
                "Key is not String, Yaml is not support this");

        getData().put((String) key, value);

    }

    @Override
    public SerializedMapData createSelfInstance() {
        return new YamlData();
    }

    @Override
    public String getMessage(Object key, boolean prefixed) {
        if (prefixed) {
            return ConfigsUtil.toColorizedMessage(getPrefix() + getString(key));
        }
        return ConfigsUtil.toColorizedMessage(getString(key));
    }

    @Override
    public String getMessage(Object key, String defaultMessage, boolean prefixed) {
        if (prefixed) {
            return ConfigsUtil.toColorizedMessage(getPrefix() + getString(key, defaultMessage));
        }
        return ConfigsUtil.toColorizedMessage(getString(key, defaultMessage));
    }

    @Override
    public void setMessage(Object key, String message, boolean prefixed) {
        if (!prefixed) {
            setObject(key, ConfigsUtil.fromColorizedMessage(message));
        }
        else {
            setObject(key, ConfigsUtil.fromColorizedMessage(message.substring(getPrefix().length())));
        }
    }

    private String getPrefix() {
        return Discomc.getPlugin().getDefaultConfig().getPrefix();
    }

}
