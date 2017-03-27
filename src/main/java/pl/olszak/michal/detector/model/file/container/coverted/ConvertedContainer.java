package pl.olszak.michal.detector.model.file.container.coverted;

import java.util.HashMap;
import java.util.Map;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class ConvertedContainer {

    private final Map<String, ConvertedFile> convertedFiles;

    public ConvertedContainer() {
        this.convertedFiles = new HashMap<>();
    }

    public void put(String key, ConvertedFile convertedFile) {
        this.convertedFiles.put(key, convertedFile);
    }

    public Map<String, ConvertedFile> getConvertedFiles() {
        return convertedFiles;
    }
}
