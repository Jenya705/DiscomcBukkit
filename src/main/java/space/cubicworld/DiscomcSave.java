package space.cubicworld;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BeanProperty;
import ch.jalu.configme.properties.Property;

public class DiscomcSave implements SettingsHolder {

    @Comment({
            "Channel id which will be present as console"
    })
    public static final Property<Long> CONSOLE_CHANNEL_ID =
            new BeanProperty<>(Long.class, "channels.console", -1L);

    @Comment({
            "Channel id which will receive codes from players"
    })
    public static final Property<Long> CONNECT_CHANNEL_ID =
            new BeanProperty<>(Long.class, "channels.connect", -1L);

    @Comment({
            "Channel id which will receive and send chat events"
    })
    public static final Property<Long> CHAT_CHANNEL_ID =
            new BeanProperty<>(Long.class, "channels.chat", -1L);

    private DiscomcSave() { /* NOTHING */}

}
