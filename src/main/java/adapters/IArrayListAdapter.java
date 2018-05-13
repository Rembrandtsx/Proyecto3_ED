package adapters;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import model.data_structures.ArrayList;
import model.data_structures.IArrayList;

public class IArrayListAdapter<T> implements JsonSerializer<IArrayList<T>>,
	JsonDeserializer<IArrayList<T>> {
	
	@Override
	public JsonElement serialize(IArrayList<T> src, Type typeOfSrc, 
				JsonSerializationContext context) {
		JsonArray array = new JsonArray();
        
		for (int i = 0; i < src.size(); i++) {
			array.add(context.serialize(src.get(i)));
		}

		return array;
	}

	@Override
	public IArrayList<T> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context)
			throws JsonParseException {
		ArrayList<T> array = new ArrayList<>();
		JsonArray jsonArray = (JsonArray) json;
		
		for (JsonElement element : jsonArray) {
			array.add(context.deserialize(element, Object.class));
		}
		
		return null;
	}

}