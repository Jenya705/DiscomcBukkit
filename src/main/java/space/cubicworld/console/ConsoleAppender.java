package space.cubicworld.console;

import net.md_5.bungee.api.ChatColor;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import space.cubicworld.DiscomcPlugin;

import java.io.Serializable;

public class ConsoleAppender extends AbstractAppender {

    public static final PatternLayout CONSOLE_PATTERN_LAYOUT = PatternLayout.newBuilder().
            withPattern("[%d{HH:mm:ss} %p]: %m").
            build();

    public static final PatternLayout PLUGIN_PATTERN_LAYOUT = PatternLayout.newBuilder().
            withPattern("[%d{HH:mm:ss} %p]: [%c] %m").
            build();

    public ConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
        start();
    }

    @Override
    public void append(LogEvent logEvent) {
        if (logEvent.getLoggerName().startsWith("net.minecraft.") || logEvent.getLoggerName().equalsIgnoreCase("minecraft")){
            DiscomcPlugin.getDiscomcPlugin().getConsoleModule()
                    .getConsoleMessages().add(CONSOLE_PATTERN_LAYOUT.toSerializable(logEvent));
        }
        else {
            DiscomcPlugin.getDiscomcPlugin().getConsoleModule()
                    .getConsoleMessages().add(PLUGIN_PATTERN_LAYOUT.toSerializable(logEvent));
        }
    }

    public static String formatMessage(String message){
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < message.length(); i++){
            char ch = message.charAt(i);
            switch(ch){
                case '*':
                case '_':
                case '~':
                case '`':
                case '|':
                case '\\':
                    messageBuilder.append("\\").append(ch);
                    break;
                case ChatColor.COLOR_CHAR:
                    i++;
                    break;
                default:
                    messageBuilder.append(ch);
                    break;
            }
        }
        return messageBuilder.toString();
    }

}
