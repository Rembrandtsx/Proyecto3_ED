package adapters;

import java.lang.reflect.Type;

import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeAdapter implements JsonSerializer<DateTime>,
	JsonDeserializer<DateTime> {
	
	@Override
	public JsonElement serialize(DateTime src, Type typeOfSrc, 
				JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	@Override
	public DateTime deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context)
			throws JsonParseException {
		return DateTime.parse(((JsonPrimitive) json).getAsString());
	}

}