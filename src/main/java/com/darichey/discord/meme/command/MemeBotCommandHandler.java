package com.darichey.discord.meme.command;

import com.darichey.discord.meme.command.api.CommandHandler;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MemeBotCommandHandler extends CommandHandler<MemeBotCommand> {

    private final String prefix;
    private final Set<Long> allowedGuilds;

    public MemeBotCommandHandler(Map<String, MemeBotCommand> commands, String prefix, Set<Long> allowedGuilds) {
        super(commands);
        this.prefix = prefix;
        this.allowedGuilds = allowedGuilds;
    }

    @Override
    public Mono<Void> handle(MessageCreateEvent messageCreateEvent) {
        return Mono.just(messageCreateEvent)
                .filter(this::shouldHandle)
                .flatMap(event -> {
                    Optional<MemeBotCommand> command = event.getMessage().getContent()
                            .map(this::getCommandName)
                            .flatMap(this::getCommand);

                    return Mono.justOrEmpty(command).flatMap(it -> it.execute(event));
                });
    }

    @Override
    public String getPrefix(MessageCreateEvent messageCreateEvent) {
        return prefix;
    }

    private boolean shouldHandle(MessageCreateEvent event) {
        return event.getGuildId().map(Snowflake::asLong).map(allowedGuilds::contains).orElse(false)
                && event.getMessage().getContent().map(it -> it.startsWith(prefix)).orElse(false);
    }

    private String getCommandName(String content) {
        int end = content.contains(" ") ? content.indexOf(" ", prefix.length()) : content.length();
        return content.substring(prefix.length(), end);
    }
}
