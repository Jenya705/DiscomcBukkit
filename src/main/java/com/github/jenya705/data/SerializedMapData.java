package com.github.jenya705.data;

import com.github.jenya705.Discomc;
import com.github.jenya705.util.ConfigsUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class SerializedMapData<T, V> implements SerializedData, MinecraftConfigData, AdditionalConfigData {

    private Map<T, V> data = new LinkedHashMap<>();

    @Override
    @SuppressWarnings("suspicious call")
    public Object getObject(Object key) {
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
        SerializedMapData<T, V> result = createSelfInstance();
        result.setData((Map<T, V>) obj);
        return result;
    }

    @Override
    @SuppressWarnings("suspicious call")
    public Object getObject(Object key, Object defaultValue) {
        return getData().getOrDefault(key, (V) defaultValue);
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
        SerializedMapData<T, V> result = createSelfInstance();
        result.setData((Map<T, V>) obj);
        return result;
    }

    @Override
    @SuppressWarnings("Unchecked")
    public void setObject(Object key, Object value) {
        getData().put((T) key, (V) value);
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

    @Override
    public Color getColor(Object key) {
        Object obj = getObject(key);
        if (obj instanceof Color) {
            return (Color) obj;
        }
        if (obj instanceof List) {
            List<Object> asList = (List<Object>) obj;
            return colorFromList(asList);
        }
        if (obj instanceof String) {
            String asString = (String) obj;
            return colorFromHex(asString);
        }
        if (obj instanceof Integer) {
            int asInteger = (int) obj;
            return new Color(asInteger, asInteger > 255*255*255);
        }
        throw new IllegalArgumentException(String.format("Object with key: %s is not Color", key));
    }

    @Override
    public Color getColor(Object key, Color defaultValue) {
        Object obj = getObject(key, defaultValue);
        if (obj instanceof Color) {
            return (Color) obj;
        }
        if (obj instanceof List) {
            List<Object> asList = (List<Object>) obj;
            return colorFromList(asList);
        }
        if (obj instanceof String) {
            String asString = (String) obj;
            return colorFromHex(asString);
        }
        if (obj instanceof Integer) {
            int asInteger = (int) obj;
            return new Color(asInteger, asInteger > 255*255*255);
        }
        throw new IllegalArgumentException(String.format("Object with key: %s is not Color", key));
    }

    @Override
    public void setColor(Object key, Color color) {
        if (color.getAlpha() == 255) {
            setObject(key, String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
        }
        else {
            setObject(key, String.format("%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        }
    }

    protected String getPrefix() {
        return Discomc.getPlugin().getDefaultConfig().getPrefix();
    }

    protected Color colorFromHex(String hex) {
        int r, g, b, a;
        if (hex.length() != 6 && hex.length() != 8) throw new IllegalArgumentException(
                String.format("Hex String is not right, string: %s", hex));
        r = integerFromHexChar(hex.charAt(0)) * 255 + integerFromHexChar(hex.charAt(1));
        g = integerFromHexChar(hex.charAt(2)) * 255 + integerFromHexChar(hex.charAt(3));
        b = integerFromHexChar(hex.charAt(4)) * 255 + integerFromHexChar(hex.charAt(5));
        if (hex.length() == 8) {
            a = integerFromHexChar(hex.charAt(6)) * 255 + integerFromHexChar(hex.charAt(7));
            return new Color(r, g, b, a);
        }
        return new Color(r, g, b);
    }

    protected Color colorFromList(List<Object> list) {
        if (list.size() != 3 && list.size() != 4) throw new IllegalArgumentException(
                String.format("List size is not right, list size: %s", list.size()));
        int r, g, b, a;
        r = (int) list.get(0);
        g = (int) list.get(1);
        b = (int) list.get(2);
        if (list.size() == 4) {
            a = (int) list.get(3);
            return new Color(r, g, b, a);
        }
        return new Color(r, g, b);
    }

    protected int integerFromHexChar(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'A' && c <= 'F') return c - 'A';
        if (c >= 'a' && c <= 'f') return c - 'a';
        throw new IllegalArgumentException(String.format("Hex Char is not right char: %s", c));
    }

    public abstract SerializedMapData<T, V> createSelfInstance();

}
