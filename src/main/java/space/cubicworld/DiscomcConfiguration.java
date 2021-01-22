package space.cubicworld;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BeanProperty;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class DiscomcConfiguration implements SettingsHolder {

    @Comment({
            "See ALL explanation about this values on github page."
    })
    public static final Property<String> BOT_TOKEN =
            newProperty("token", "PUT UR TOKEN HERE!");

    public static final Property<Long> MAIN_SERVER_ID =
            new BeanProperty<>(Long.class, "mainServerID", -1L);

    public static final Property<Boolean> SQL_ENABLED =
            newProperty("sql.enabled", false);

    public static final Property<String> SQL_USER =
            newProperty("sql.user", "root");

    public static final Property<String> SQL_PASSWORD =
            newProperty("sql.password", "");

    public static final Property<String> SQL_HOST =
            newProperty("sql.host", "127.0.0.1");

    public static final Property<String> SQL_PORT =
            newProperty("sql.port", "5432");

    public static final Property<String> SQL_DATABASE =
            newProperty("sql.database", "minecraft");

    @Comment({
            "Integrated: mysql, postgresql",
            "CaSe SeNsItIvE"
    })
    public static final Property<String> SQL_TYPE =
            newProperty("sql.type", "postgresql");

    public static final Property<Long> CREATION_CATEGORY_ID =
            new BeanProperty<>(Long.class, "creationCategoryID", -1L);

    public static final Property<Boolean> MULTI_CHAT_ENABLED =
            newProperty("multiChat.enabled", true);

    public static final Property<Boolean> PREMIUM_UUIDS_GET =
            newProperty("multiChat.getPremiumUuids", false);

    public static final Property<String> MULTI_CHAT_WEBHOOK_URL =
            newProperty("multiChat.webhookURL", "");

    public static final Property<Boolean> CONNECT_ENABLED =
            newProperty("connect.enabled", true);

    public static final Property<Long> CODE_REMOVE_SECONDS =
            new BeanProperty(Long.class, "connect.codeRemoveSeconds", 300);

    public static final Property<Boolean> NICKNAME_CHANGE_ENABLED =
            newProperty("nicknameChange.enabled", false);

    public static final Property<String> NICKNAME_CHANGE_PATTERN =
            newProperty("nicknameChange.pattern", "{0} | {1}");

    private DiscomcConfiguration(){ /* NOTHING */}

}
