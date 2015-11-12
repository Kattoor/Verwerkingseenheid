package be.kdg.schelderadarketen.verwerkingseenheid.engine.utils;

import com.google.gson.Gson;

public class GsonDeserializer {

    public static <T> T deserialize(String json, Class<T> cls) {
        return new Gson().fromJson(json, cls);
    }
}
