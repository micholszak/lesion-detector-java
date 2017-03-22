package pl.olszak.michal.detector.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public final class DetectorFolders {
    private static final Logger logger = LoggerFactory.getLogger(DetectorFolders.class);

    private static final String JSON_FOLDER = "./json";
    public static final String TEST_RESOURCES_FOLDER = "./test_resources";
    public static final String LEARNING_RESOURCES_FOLDER = "./learning_resources";
    public static final String MASK_RESOURCES_FOLDER = "./mask_resources";
    public static final String RESOULTS_FOLDER = "./resoults";
    public static final String ENHANCEMENTS = "./ehancements";

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
        try (FileWriter writer = new FileWriter(new File(JSON_FOLDER, filename))) {
            writer.write(json);
            logger.info("Contents of json saved to file %s", filename);
        } catch (IOException e) {
            logger.error(String.format("Could not save json under %s with name %s", JSON_FOLDER, filename));
        }
    }


}
