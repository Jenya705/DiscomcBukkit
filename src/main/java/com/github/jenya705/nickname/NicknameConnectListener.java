package com.github.jenya705.nickname;

import com.github.jenya705.Discomc;
import com.github.jenya705.connect.AsyncConnectSuccessEvent;
import discord4j.core.object.entity.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NicknameConnectListener implements Listener {

    @EventHandler
    public void connectSuccess(AsyncConnectSuccessEvent event) {
        Discomc discomc = Discomc.getPlugin();
        NicknameModule nicknameModule = discomc.getNicknameModule();
        Message message = event.getAttachedMessage();
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPlayerUUID());
        nicknameModule.updateNickname(message.getAuthor().get().getId(), player.getName());
    }

}
