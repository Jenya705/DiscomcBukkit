package com.github.jenya705.connect;

import discord4j.core.object.entity.Message;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class AsyncConnectSuccessEvent extends Event implements Cancellable {

    private final static HandlerList handlers = new HandlerList();

    private final Message attachedMessage;
    @Setter
    private UUID playerUUID;
    @Setter
    private boolean cancelled;

    public AsyncConnectSuccessEvent(Message attachedMessage, UUID playerUUID, boolean async) {
        super(async);
        this.attachedMessage = attachedMessage;
        this.playerUUID = playerUUID;
    }

    public static HandlerList getHandlerList() { return handlers; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
