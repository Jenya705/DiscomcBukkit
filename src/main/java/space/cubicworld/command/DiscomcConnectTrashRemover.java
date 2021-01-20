package space.cubicworld.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import space.cubicworld.discord.DiscordConnectHandler;

@Getter
@Setter
public class DiscomcConnectTrashRemover extends BukkitRunnable {

    private short code;

    public DiscomcConnectTrashRemover(short code){
        setCode(code);
    }

    @Override
    public void run() {
        DiscordConnectHandler.removeCode(getCode());
    }

}
