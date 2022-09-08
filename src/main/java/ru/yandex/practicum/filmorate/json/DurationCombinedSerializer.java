package ru.yandex.practicum.filmorate.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

@JsonComponent
public class DurationCombinedSerializer {

    public static class DurationJsonSerializer extends JsonSerializer<Duration> {

        @Override
        public void serialize(Duration duration, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            if (Objects.isNull(duration)) {
                return;
            }
            jsonGenerator.writeNumber(duration.toMinutes());
        }
    }

    public static class DurationJsonDeserializer extends JsonDeserializer<Duration> {

        @Override
        public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            long duration = jsonParser.getLongValue();
            return Duration.ofMinutes(duration);
        }
    }
}
