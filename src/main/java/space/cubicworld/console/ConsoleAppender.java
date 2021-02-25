package space.cubicworld.console;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import space.cubicworld.DiscomcPlugin;

import java.io.Serializable;

public class ConsoleAppender extends AbstractAppender {

    public static final PatternLayout PATTERN_LAYOUT = PatternLayout.newBuilder().
            withPattern("[%d{HH:mm:ss} %p] %m").
            build();

    public ConsoleAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
        start();
    }

    @Override
    public void append(LogEvent logEvent) {
        DiscomcPlugin.getDiscomcPlugin().getConsoleModule()
                .getConsoleMessages().add(PATTERN_LAYOUT.toSerializable(logEvent));
    }

    public static String formatMessage(String message){
        StringBuilder messageBuilder = new StringBuilder();
        for (char ch: message.toCharArray()){
            switch(ch){
                case '*':
                case '_':
                case '~':
                case '`':
                case '|':
                case '\\':
                    messageBuilder.append("\\").append(ch);
                    break;
                default:
                    messageBuilder.append(ch);
                    break;
            }
        }
        return messageBuilder.toString();
    }

}
