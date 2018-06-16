package com.darichey.discord.meme.command;

import discord4j.commands.BaseCommand;

public interface MemeBotCommand extends BaseCommand {

    String getDescription();
}
