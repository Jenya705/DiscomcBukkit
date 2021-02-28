package space.cubicworld.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.cubicworld.DiscomcMessages;
import space.cubicworld.DiscomcPlugin;
import space.cubicworld.util.Pair;

import java.text.MessageFormat;
import java.util.List;

public class HelpCommand extends DiscomcAdminCommand{

    public static final int COMMANDS_ON_PAGE = 5;

    public HelpCommand() {
        super("send help menu", "discomc.help");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        int page = 0;
        if (args.length >= 2){
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {}
        }
        DiscomcPlugin discomcPlugin = DiscomcPlugin.getDiscomcPlugin();
        DiscomcMessages discomcMessages = discomcPlugin.getDiscomcMessages();
        DiscomcCommand discomcCommand = discomcPlugin.getDiscomcCommand();
        List<Pair<String, DiscomcAdminCommand>> commands = discomcCommand.getCommands();
        page %= (commands.size() / COMMANDS_ON_PAGE) + 1;
        TextComponent textComponent = new TextComponent();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MessageFormat.format(
                discomcMessages.getHelpFooter(), Integer.toString(page))).append("\n");
        for (int i = COMMANDS_ON_PAGE * page; i < COMMANDS_ON_PAGE * (page + 1) && i < commands.size(); ++i){
            Pair<String, DiscomcAdminCommand> currentCommand = commands.get(i);
            stringBuilder.append(MessageFormat.format(
                    discomcMessages.getHelpElement(), currentCommand.getKey(),
                    currentCommand.getValue().getDescription())).append("\n");
        }
        textComponent.setText(stringBuilder.toString());
        TextComponent previousComponent = new TextComponent(discomcMessages.getHelpPrevious());
        previousComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/%s help %d", DiscomcCommand.DISCOMC_COMMAND, page-1)));
        TextComponent nextComponent = new TextComponent(discomcMessages.getHelpNext());
        nextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/%s help %d", DiscomcCommand.DISCOMC_COMMAND, page+1)));
        textComponent.addExtra(previousComponent);
        textComponent.addExtra(nextComponent);
        commandSender.spigot().sendMessage(textComponent);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}
