package com.darichey.discord.meme.command;

import com.darichey.discord.meme.MemeFetcher;
import discord4j.commands.CommandErrorHandler;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class MemeCommand implements MemeBotCommand {

    private final MemeFetcher memeFetcher;

    public MemeCommand(MemeFetcher memeFetcher) {
        this.memeFetcher = memeFetcher;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent messageCreateEvent, CommandErrorHandler commandErrorHandler) {
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
