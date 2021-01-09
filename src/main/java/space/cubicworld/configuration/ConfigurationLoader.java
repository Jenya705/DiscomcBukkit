package space.cubicworld.configuration;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@UtilityClass
public final class ConfigurationLoader {

    public static Object loadConfiguration(Class configClass, FileConfiguration config) throws InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = configClass.getDeclaredFields();
        Object configObject = configClass.newInstance();
        for (Field field: fields){
            YMLSerializedName serializedName = field.getAnnotation(YMLSerializedName.class);
            if (serializedName == null) continue;
            String name = serializedName.name();
            Method method = configClass.getMethod("set" + Character.toUpperCase(field.getName().charAt(0)) +
                    field.getName().substring(1, field.getName().length()), field.getType());
            method.invoke(configObject, field.getClass().cast(config.get(name)));
        }
        return configObject;
    }

}
