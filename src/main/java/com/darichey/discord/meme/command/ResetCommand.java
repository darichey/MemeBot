package com.darichey.discord.meme.command;

import com.darichey.discord.meme.MemeFetcher;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class ResetCommand implements MemeBotCommand {

    private final MemeFetcher memeFetcher;

    public ResetCommand(MemeFetcher memeFetcher) {
        this.memeFetcher = memeFetcher;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent messageCreateEvent) {
        return messageCreateEvent.getMessage().getChannel()
                .flatMap(channel -> {
                    memeFetcher.reset();
                    return channel.createMessage(spec -> spec.setContent("\uD83D\uDC4C")); // 👌
                })
                .then();
    }

    @Override
    public String getDescription() {
        return "Forcefully resets the meme getter. Fresh memes guaranteed.";
    }
}
