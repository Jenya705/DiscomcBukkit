package space.cubicworld.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import space.cubicworld.discord.DiscordConnectHandler;

@Getter
@Setter
public class ConnectTrashRemover extends BukkitRunnable {

    private short code;

    public ConnectTrashRemover(short code){
        setCode(code);
    }

    @Override
    public void run() {
        ConnectCommand.removeAlreadyWrotePlayer(
                DiscordConnectHandler.removeCode(getCode()));
    }

}
