package com.darichey.discord.meme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import discord4j.core.object.util.Snowflake;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class BotConfig {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new SimpleModule().addDeserializer(Snowflake.class, new SnowflakeDeserializer()));

    private String discordToken;
    private String redditClientId;
    private String redditSecret;
    private String redditUUID;
    private String redditUserAgent;
    private Set<String> subreddits;
    private Set<Snowflake> allowedGuilds;
    private String prefix;
    private int resetTime;

    static BotConfig fromJson(Path json) throws IOException {
        try (Stream<String> lines = Files.lines(json)) {
            return fromJson(lines.collect(Collectors.joining()));
        }
    }

    static BotConfig fromJson(String json) throws IOException {
        return mapper.readValue(json, BotConfig.class);
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public String getRedditClientId() {
        return redditClientId;
    }

    public String getRedditSecret() {
        return redditSecret;
    }

    public String getRedditUUID() {
        return redditUUID;
    }

    public String getRedditUserAgent() {
        return redditUserAgent;
    }

    public Set<String> getSubreddits() {
        return subreddits;
    }

    public Set<Snowflake> getAllowedGuilds() {
        return allowedGuilds;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getResetTime() {
        return resetTime;
    }
}
