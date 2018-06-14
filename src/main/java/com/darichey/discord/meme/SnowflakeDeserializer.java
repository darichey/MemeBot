package com.darichey.discord.meme;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import discord4j.core.object.util.Snowflake;

import java.io.IOException;

class SnowflakeDeserializer extends StdDeserializer<Snowflake> {

    SnowflakeDeserializer() {
        super(Snowflake.class);
    }

    @Override
    public Snowflake deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Snowflake.of(p.getValueAsString());
    }
}
