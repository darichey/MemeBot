package com.darichey.discord.meme.command;

import com.darichey.discord.meme.MemeBot;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.awt.Color;
import java.util.Map;

public class HelpCommand implements MemeBotCommand {

    private final EmbedCreateSpec helpEmbed;

    public HelpCommand(Map<String, MemeBotCommand> commands) {
        EmbedCreateSpec helpEmbed = new EmbedCreateSpec();

        helpEmbed.setAuthor(MemeBot.NAME, MemeBot.REPO_URL, null);
        helpEmbed.setColor(new Color(54, 57, 62).getRGB() & 0xFFFFFF);

        for (String name : commands.keySet()) {
            String description = commands.get(name).getDescription();
            helpEmbed.addField(name, description, true);
        }

        this.helpEmbed = helpEmbed;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent messageCreateEvent) {
        return messageCreateEvent.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage(spec -> spec.setEmbed(helpEmbed)))
                .then();
    }

    @Override
    public String getDescription() {
        return "Lists commands.";
    }
}
