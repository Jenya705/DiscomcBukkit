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
            new BeanProperty<Long>(Long.class, "mainServerID", -1L);

    @Comment({
            "The bot will create all he need channels in this category",
            "if this value -1 the bot will create category with name \"mc\"",
            "and change this value to it id",
            "you can bring channels in another categories without lose it by bot"
    })
    public static final Property<Long> CREATION_CATEGORY_ID =
            new BeanProperty<Long>(Long.class, "creationCategoryID", -1L);

    private DiscomcConfiguration(){ /* NOTHING */}

}
