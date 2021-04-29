package com.github.jenya705.multichat;

import com.github.jenya705.Discomc;
import com.github.jenya705.discord.DiscordModule;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.Color;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.renderer.ComponentRenderer;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class MentionSerializer {

    @Getter
    private static final Pattern pattern = Pattern.compile("<(@!|@&|#)(\\d{1,19})>|@(here|everyone)");
    private static final TextColor globalMentionsColor = TextColor.color(Color.CYAN.getRGB());
    private static final TextColor channelsColor = TextColor.color(Color.BLUE.getRGB());

    public static Component fromString(String from) {
        Discomc discomc = Discomc.getPlugin();
        MultiChatModule multiChatModule = discomc.getMultiChatModule();
        Matcher matcher = pattern.matcher(from);
        Component component = null;
        int previousEnd = 0;
        while (matcher.find()) {
            String localMentionStart = matcher.group(1);
            String localMentionIDAsString = matcher.group(2);
            Component mentionComponent;
            if (localMentionStart == null) {
                mentionComponent = createGlobalMentionComponent(matcher.group(0));
            }
            else {
                mentionComponent = createLocalMentionComponent(
                        localMentionStart, localMentionIDAsString);
            }
            if (mentionComponent != null) {
                if (matcher.start() != 0) {
                    component = appendComponentWithNullCheck(component, Component.text(
                            from.substring(previousEnd, matcher.start())));
                }
                component = appendComponentWithNullCheck(component, mentionComponent);
                previousEnd = matcher.end();
            }
        }
        if (component == null) component = Component.text(from);
        else if (previousEnd < from.length()) component = component.append(
                Component.text(from.substring(previousEnd)));
        return component;
    }

    private static Component appendComponentWithNullCheck(Component current, Component toAppend) {
        return (current == null) ? toAppend : current.append(toAppend);
    }

    private static Component createLocalMentionComponent(String localMentionStart, String localMentionIDAsString) {
        Discomc discomc = Discomc.getPlugin();
        DiscordModule discordModule = discomc.getDiscordModule();
        MultiChatModule multiChatModule = discomc.getMultiChatModule();
        Component component;
        if (localMentionStart.equals("@!")) {
            Optional<Member> mentionedMember;
            try {
                mentionedMember = discordModule.getMainGuild()
                        .getMemberById(Snowflake.of(localMentionIDAsString))
                        .blockOptional();
            } catch (ClientException e) { return null; }
            if (!mentionedMember.isPresent()) return null;
            component = Component.text("@" + mentionedMember.get().getDisplayName());
            if (multiChatModule.getConfig().isMentionColors()) {
                component = component.color(TextColor.color(
                        mentionedMember.get().getColor()
                                .block().getRGB()));
            }
        }
        else if (localMentionStart.equals("@&")) {
            Optional<Role> mentionedRole;
            try {
                mentionedRole = discordModule.getMainGuild()
                        .getRoleById(Snowflake.of(localMentionIDAsString))
                        .blockOptional();
            } catch (ClientException e) { return null; }
            if (!mentionedRole.isPresent() ||
                    mentionedRole.get().getName().equals("") ||
                    !mentionedRole.get().isMentionable()) return null;
            component = Component.text("@" + mentionedRole.get().getName());
            if (multiChatModule.getConfig().isMentionColors()) {
                component = component.color(TextColor.color(
                        mentionedRole.get().getColor().getRGB()));
            }
        }
        else {
            Optional<GuildChannel> channel;
            try {
                channel = discordModule.getMainGuild()
                        .getChannelById(Snowflake.of(localMentionIDAsString))
                        .filter(it -> it.getType() == Channel.Type.GUILD_TEXT)
                        .blockOptional();
            } catch (ClientException e) { return null; }
            if (!channel.isPresent()) return null;
            component = Component.text("#" + channel.get().getName());
            if (multiChatModule.getConfig().isMentionColors()) {
                component = component.color(channelsColor);
            }
        }
        return component;
    }

    private static Component createGlobalMentionComponent(String mention) {
        Discomc discomc = Discomc.getPlugin();
        MultiChatModule multiChatModule = discomc.getMultiChatModule();
        Component mentionComponent = Component.text(mention);
        if (multiChatModule.getConfig().isMentionColors()) {
            mentionComponent = mentionComponent
                    .color(globalMentionsColor);
        }
        return mentionComponent;
    }

}
