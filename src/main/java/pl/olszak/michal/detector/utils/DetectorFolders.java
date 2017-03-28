package pl.olszak.michal.detector.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public final class DetectorFolders {
    private static final Logger logger = LoggerFactory.getLogger(DetectorFolders.class);

    private static final String JSON_FOLDER = "json";
    public static final String TEST_RESOURCES_FOLDER = "test_set";
    public static final String LEARNING_RESOURCES_FOLDER = "images";
    public static final String MASK_RESOURCES_FOLDER = "mask";
    public static final String RESOULTS_FOLDER = "results";
    public static final String ENHANCEMENTS = "ehancements";
    public static final String JSON_EXTENSION = ".json";
    public static final String PROBABILITY_MAP = "probability-map-%d";

    public static String readJsonFile(String filename) {
        String json = "";
        File file = new File(JSON_FOLDER, filename);

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            json = IOUtils.toString(reader);
            logger.info(String.format("Read contents from file %s", file.getAbsolutePath()));
        } catch (IOException e) {
            logger.error(String.format("Could not read json from file %s", file.getAbsolutePath()), e);
        }

        return json;
    }

    public static void saveJson(String filename, String json) {
        File file = new File(JSON_FOLDER, filename + JSON_EXTENSION);
        try {
            FileUtils.write(file, json);
            logger.info(String.format("Contents of json saved to file %s", filename + JSON_EXTENSION));
        } catch (IOException e) {
            logger.error(String.format("Could not save json under %s with name %s", JSON_FOLDER, filename));
        }
    }


}
