package com.darichey.discord.meme;

import com.darichey.discord.meme.command.*;
import discord4j.core.ClientBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.store.jdk.JdkStoreService;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MemeBot {

    public static final String NAME = "Meme Bot";
    public static final String REPO_URL = "https://github.com/GrandPanda/MemeBot";

    private final BotConfig config;

    public static void main(String[] args) throws IOException {
        new MemeBot(BotConfig.fromJson(Paths.get(args[0]))).start().block();
    }

    private MemeBot(BotConfig config) {
        this.config = config;
    }

    private Mono<Void> start() {
        DiscordClient discord = new ClientBuilder(config.getDiscordToken())
                .setStoreService(new JdkStoreService())
                .build();

        MemeFetcher memeFetcher = new MemeFetcher(config.getRedditClientId(), config.getRedditSecret(),
                                                  config.getRedditUUID(), config.getSubreddits(),
                                                  config.getRedditUserAgent(), config.getResetTime());

        Map<String, MemeBotCommand> commands = new HashMap<>(2);
        commands.put("meme", new MemeCommand(memeFetcher));
        commands.put("reset", new ResetCommand(memeFetcher));
        commands.put("help", new HelpCommand(commands));

        MemeBotCommandHandler commandHandler = new MemeBotCommandHandler(commands, config.getPrefix(),
                                                                         config.getAllowedGuilds());

        Mono<Void> handleCommands = discord.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(commandHandler::handle)
                .then();

        return discord.login().and(handleCommands);
    }
}
