package com.darichey.discord.meme.command;

import com.darichey.discord.meme.MemeBot;
import discord4j.commands.CommandErrorHandler;
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
	public void execute(MessageCreateEvent messageCreateEvent, CommandErrorHandler commandErrorHandler) {
    	messageCreateEvent.getMessage().getChannel()
			    .flatMap(channel -> channel.createMessage(spec -> spec.setEmbed(helpEmbed)))
			    .subscribe();
	}

    @Override
    public String getDescription() {
        return "Lists commands.";
    }
}
