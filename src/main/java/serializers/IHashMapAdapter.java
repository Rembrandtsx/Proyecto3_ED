package serializers;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import model.data_structures.IHashMap;
import model.data_structures.SeparateChainingHashMap;

public class IHashMapAdapter<K extends Comparable<K>, V> 
	implements JsonSerializer<IHashMap<K, V>>, JsonDeserializer<IHashMap<K, V>> {

	@Override
	public JsonElement serialize(IHashMap<K, V> src, Type typeOfSrc, 
				JsonSerializationContext context) {
		JsonObject object = new JsonObject();
        
		Iterator it = src.keys();
		while (it.hasNext()) {
			Comparable<K> key = (Comparable<K>) it.next();
			try {
				object.add(key.toString(), 
						context.serialize(src.get((K) key)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return object;
	}

	@Override
	public IHashMap<K, V> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context)
			throws JsonParseException {
		SeparateChainingHashMap<K, V> hashMap = new SeparateChainingHashMap<>();
		JsonObject object = (JsonObject) json;
		
		for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
			try {
				hashMap.put((K) entry.getKey(),
						context.deserialize(entry.getValue(), typeOfT));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}