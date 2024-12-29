package com.first.task_manager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Base64;

public class Base64Serializer extends JsonSerializer<byte[]> {

    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Convert byte[] to Base64 string
        String base64 = Base64.getEncoder().encodeToString(value);
        // Write Base64 string to JSON
        gen.writeString(base64);
    }
}

