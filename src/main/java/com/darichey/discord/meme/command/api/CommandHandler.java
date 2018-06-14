package com.darichey.discord.meme.command.api;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class CommandHandler<C extends Command> {

    protected final Map<String, C> commands;

    public CommandHandler(Map<String, C> commands) {
        this.commands = commands;
    }

    public abstract Mono<Void> handle(MessageCreateEvent messageCreateEvent);
    public abstract String getPrefix(MessageCreateEvent messageCreateEvent);

    public Optional<C> getCommand(String name) {
        return Optional.ofNullable(commands.get(name));
    }

    public Map<String, C> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
