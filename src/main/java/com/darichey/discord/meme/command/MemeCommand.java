package com.darichey.discord.meme.command;

import com.darichey.discord.meme.MemeFetcher;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class MemeCommand implements MemeBotCommand {

    private final MemeFetcher memeFetcher;

    public MemeCommand(MemeFetcher memeFetcher) {
        this.memeFetcher = memeFetcher;
    }

    public Mono<Void> execute(MessageCreateEvent messageCreateEvent) {
        return messageCreateEvent.getMessage().getChannel()
                .zipWhen(channel -> memeFetcher.nextMeme())
                .flatMap(t -> {
                    String content = "`" + t.getT2().getTitle() + "`: " + t.getT2().getUrl();
                    return t.getT1().createMessage(spec -> spec.setContent(content));
                })
                .then();
    }

    @Override
    public String getDescription() {
        return "Fetches a dank meme from reddit.";
    }
}
