package space.cubicworld;

import lombok.Getter;
import org.shanerx.mojang.Mojang;

import java.util.logging.Level;

@Getter
public class MojangApi {

    private Mojang api;

    public MojangApi(){
        api = new Mojang().connect();
    }

    public String getPremiumUUID(String playerNickname){

        if (api.getStatus(Mojang.ServiceType.API_MOJANG_COM) != Mojang.ServiceStatus.GREEN){
            DiscomcPlugin discomcPlugin = DiscomcPlugin.getInstance();
            discomcPlugin.getLogger().log(Level.WARNING, "The Mojang api server is not available!");
            return null;
        }
        return api.getUUIDOfUsername(playerNickname);

    }

}
