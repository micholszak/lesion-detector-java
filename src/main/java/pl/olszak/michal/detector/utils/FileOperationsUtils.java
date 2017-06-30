package pl.olszak.michal.detector.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.exception.FolderCreationException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * @author molszak
 *         created on 30.06.2017.
 */
public class FileOperationsUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileOperationsUtils.class);

    public static final String FOLDER_PREFIX = "reduction-%d";

    public static String createDestinationFolder(ColorReduce reduce, String destination) throws FolderCreationException {
        File file = new File(destination, String.format(Locale.getDefault(), FOLDER_PREFIX, reduce.getValue()));
        try {
            FileUtils.forceMkdir(file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Should not happen, the directory wasn't created", e);
            throw new FolderCreationException("Could not create directory");
        }
    }
}
