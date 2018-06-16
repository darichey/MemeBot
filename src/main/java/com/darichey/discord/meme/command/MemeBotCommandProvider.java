package com.darichey.discord.meme.command;

import discord4j.commands.BaseCommand;
import discord4j.commands.CommandProvider;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.util.Snowflake;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MemeBotCommandProvider implements CommandProvider {

    private final Map<String, MemeBotCommand> commands;
    private final String prefix;
    private final Set<Long> allowedGuilds;

    public MemeBotCommandProvider(Map<String, MemeBotCommand> commands, String prefix, Set<Long> allowedGuilds) {
        this.commands = commands;
        this.prefix = prefix;
        this.allowedGuilds = allowedGuilds;
    }

    @Override
    public Optional<? extends BaseCommand> provide(MessageCreateEvent messageCreateEvent) {
        return messageCreateEvent.getGuildId()
                .map(Snowflake::asLong)
                .filter(allowedGuilds::contains)
                .flatMap(g -> messageCreateEvent.getMessage().getContent())
                .filter(c -> c.startsWith(prefix))
                .map(this::getCommandName)
                .map(commands::get);
    }

    private String getCommandName(String content) {
        int end = content.contains(" ") ? content.indexOf(" ", prefix.length()) : content.length();
        return content.substring(prefix.length(), end);
    }
}
