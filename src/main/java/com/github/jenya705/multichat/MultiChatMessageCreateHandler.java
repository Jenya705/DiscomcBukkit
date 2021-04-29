package com.github.jenya705.multichat;

import com.github.jenya705.Discomc;
import com.github.jenya705.util.ConfigsUtil;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Attachment;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

import java.text.MessageFormat;
import java.util.function.Consumer;

public class MultiChatMessageCreateHandler implements Consumer<MessageCreateEvent> {

    @Override
    public void accept(MessageCreateEvent messageCreateEvent) {
        Message message = messageCreateEvent.getMessage();
        if (!messageCreateEvent.getMember().isPresent()) return;
        Member member = messageCreateEvent.getMember().get();
        String nickname = member.getDisplayName();
        Discomc discomc = Discomc.getPlugin();
        MultiChatModule multiChatModule = discomc.getMultiChatModule();
        String stringMessage = MessageFormat.format(
                multiChatModule.getConfig().getDiscordToMinecraftMessagePattern(),
                nickname, message.getContent());
        if (multiChatModule.getConfig().isDiscordAttachments() && !message.getAttachments().isEmpty()) {
            stringMessage += "\n";
        }
        Component componentMessage;
        if (multiChatModule.getConfig().isDiscordMentions()) {
            componentMessage = MiniMessage.markdown().parse(MiniMessage.withMarkdownFlavor(DiscordFlavor.get())
                    .serialize(MentionSerializer.fromString(stringMessage)));
        }
        else {
            componentMessage = Component.text(MiniMessage.withMarkdownFlavor(DiscordFlavor.get())
                    .serialize(Component.text(stringMessage)));
        }
        for (Attachment attachment: message.getAttachments()) {
            componentMessage = componentMessage.append(Component
                        .text(MessageFormat.format(
                                multiChatModule.getConfig().getAttachmentPattern(),
                                attachment.getFilename()))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, attachment.getUrl()))
            );
        }
        Bukkit.broadcast(componentMessage, "discomc.message.discord");
    }

}
