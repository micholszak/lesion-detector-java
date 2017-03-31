package pl.olszak.michal.detector.model.loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pl.olszak.michal.detector.model.data.ColorProbabilityMap;
import pl.olszak.michal.detector.utils.DetectorFolders;

import java.lang.reflect.Type;

/**
 * @author molszak
 *         created on 31.03.2017.
 */
public class ProbabilityMapLoader implements Loader<ColorProbabilityMap> {

    private final String filename;
    private final Gson gson;

    public ProbabilityMapLoader(String filename, Gson gson) {
        this.filename = filename;
        this.gson = gson;
    }

    @Override
    public ColorProbabilityMap load() {
        String json = DetectorFolders.readJsonFile(filename);
        Type type = new TypeToken<ColorProbabilityMap>() {
        }.getType();

        return gson.fromJson(json, type);
    }
}
