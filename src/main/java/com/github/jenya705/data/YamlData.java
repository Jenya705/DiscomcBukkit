package com.github.jenya705.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class YamlData implements SerializedData {

    private static final Yaml yaml = new Yaml();

    @Setter(AccessLevel.PROTECTED)
    private Map<String, Object> data = new LinkedHashMap<>();

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
        return data.get(key);
    }

    @Override
    public Boolean getBoolean(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Boolean) return (boolean) obj;
        throw new IllegalArgumentException(String.format(
                "Boolean with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public String getString(Object key) {
        Object obj = getObject(key);
        if (obj instanceof String) return (String) obj;
        throw new IllegalArgumentException(String.format(
                "String with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public Byte getByte(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Integer) return ((Integer) obj).byteValue();
        throw new IllegalArgumentException(String.format(
                "Byte with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public Short getShort(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Integer) return ((Integer) obj).shortValue();
        throw new IllegalArgumentException(String.format(
                "Short with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public Integer getInteger(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Integer) return (Integer) obj;
        throw new IllegalArgumentException(String.format(
                "Integer with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public Long getLong(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Long) return (Long) obj;
        throw new IllegalArgumentException(String.format(
                "Long with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public Float getFloat(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Double) return ((Double) obj).floatValue();
        throw new IllegalArgumentException(String.format(
                "Float with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    public Double getDouble(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Double) return (Double) obj;
        throw new IllegalArgumentException(String.format(
                "Double with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getList(Object key) {
        Object obj = getObject(key);
        if (obj instanceof List) return (List<Object>) obj;
        throw new IllegalArgumentException(String.format(
                "List with key %s is not exist. Value: %s", key, obj));
    }

    @Override
    @SuppressWarnings("unchecked")
    public SerializedData getDirectory(Object key) {
        Object obj = getObject(key);
        if (!(obj instanceof Map)) throw new IllegalArgumentException(
                String.format("Data with key %s is not exist. Value: %s", key, obj));
        YamlData result = new YamlData();
        result.setData((Map<String, Object>) obj);
        return result;
    }

    @Override
    public Object getObject(Object key, Object defaultValue) {

        if (!(key instanceof String)) throw new IllegalArgumentException(
                "Key is not String, Yaml is not support this");
        // this is protected from null pointer exception
        return getData().getOrDefault(key, defaultValue);

    }

    @Override
    public Boolean getBoolean(Object key, boolean defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Boolean) return (Boolean) obj;
        return defaultValue;
    }

    @Override
    public String getString(Object key, String defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof String) return (String) obj;
        return defaultValue;
    }

    @Override
    public Byte getByte(Object key, byte defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Integer) return ((Integer) obj).byteValue();
        return defaultValue;
    }

    @Override
    public Short getShort(Object key, short defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Integer) return ((Integer) obj).shortValue();
        return defaultValue;
    }

    @Override
    public Integer getInteger(Object key, int defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Integer) return (Integer) obj;
        return defaultValue;
    }

    @Override
    public Long getLong(Object key, long defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Long) return (Long) obj;
        return defaultValue;
    }

    @Override
    public Float getFloat(Object key, float defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Double) return ((Double) obj).floatValue();
        return defaultValue;
    }

    @Override
    public Double getDouble(Object key, double defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Double) return (Double) obj;
        return defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getList(Object key, List<Object> defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof List) return (List<Object>) obj;
        return defaultValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SerializedData getDirectory(Object key, SerializedData defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (!(obj instanceof Map)) return defaultValue;
        YamlData result = new YamlData();
        result.setData((Map<String, Object>) obj);
        return result;
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
}
