package space.cubicworld;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BeanProperty;
import ch.jalu.configme.properties.Property;

import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class DiscomcConfiguration implements SettingsHolder {

    @Comment({
            "Token of discord bot get it on discord dev portal."
    })
    public static final Property<String> BOT_TOKEN =
            newProperty("token", "PUT UR TOKEN HERE!");

    @Comment({
            "Server id where the bot will work"
    })
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
            newProperty("sql.type", "mysql");

    @Comment({
            "The bot will create all he need channels in this category",
            "if this value -1 the bot will create category with name \"discomc\"",
            "and change this value to it id",
            "you can bring channels in another categories without lose it by bot"
    })
    public static final Property<Long> CREATION_CATEGORY_ID =
            new BeanProperty<>(Long.class, "creationCategoryID", -1L);

    @Comment({
            "If the value is true bot will send messages from minecraft to discord",
            "and from discord to minecraft."
    })
    public static final Property<Boolean> MULTI_CHAT_ENABLED =
            newProperty("multiChat.enabled", true);

    public static final Property<Boolean> CONNECT_ENABLED =
            newProperty("connect.enabled", true);

    public static final Property<Long> CODE_REMOVE_SECONDS =
            new BeanProperty(Long.class, "connect.codeRemoveSeconds", 300);

    private DiscomcConfiguration(){ /* NOTHING */}

}
