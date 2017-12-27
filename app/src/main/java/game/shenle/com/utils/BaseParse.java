package game.shenle.com.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 如果无用数据量多，推荐手动解析封装数据
 * 
 * @author shenle
 * 
 * @param <T>
 */
public class BaseParse<T> {
	/**
	 * 在json数据很庞大，而我们用到的不多，并且多次用到的情况，可以自定义TypeAdapter，不过如果真那样还是手动解析，不推荐用Gson（参考用
	 * ）
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public Gson setMyGson(String jsonString, Class<T> cls) {
		Gson gson = new GsonBuilder().registerTypeAdapter(cls,
				new TypeAdapter<T>() {
					@Override
					public T read(JsonReader in) throws IOException {
						// 手动解析成T对象
						return null;
					}

					@Override
					public void write(JsonWriter out, T src) throws IOException {
					}
				}.nullSafe()).create();
		return gson;
	}

	public static <T> T parse(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> List<T> getPersons(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
			};
			Type type = typeToken.getType();
			list = gson.fromJson(jsonString, type);
		} catch (Exception e) {
		}
		return list;
	}

	public static <T> List<Map<String, Object>> listKeyMaps(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
		} catch (Exception e) {
		}
		return list;
	}
}
