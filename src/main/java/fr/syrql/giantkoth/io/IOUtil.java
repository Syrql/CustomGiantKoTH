package fr.syrql.giantkoth.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.syrql.giantkoth.data.KothPointsData;

import java.lang.reflect.Modifier;

public class IOUtil {

    private final Gson gson;

    public IOUtil() {
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
                .enableComplexMapKeySerialization().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                .serializeSpecialFloatingPointValues().create();
    }

    public String serializeGiantKoTH(KothPointsData citadelData) {
        return this.gson.toJson(citadelData);
    }

    public KothPointsData deserializeCitadel(String json) {
        return this.gson.fromJson(json, KothPointsData.class);
    }
}

