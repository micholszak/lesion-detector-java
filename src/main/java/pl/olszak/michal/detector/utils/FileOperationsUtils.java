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

    private static final String REDUCTION_FOLDER_PREFIX = "reduction-%d";
    private static final String THRESHOLD_FOLDER_PREFIX = "threshold-%d";

    public static String createDestinationFolder(ColorReduce reduce, String destination) throws FolderCreationException {
        File file = new File(destination, String.format(Locale.getDefault(), REDUCTION_FOLDER_PREFIX, reduce.getValue()));
        try {
            FileUtils.forceMkdir(file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Should not happen, the directory wasn't created", e);
            throw new FolderCreationException("Could not create directory");
        }
    }

    public static String createDestinationFolder(int threshold, String destination) throws FolderCreationException {
        File file = new File(destination, String.format(Locale.getDefault(), THRESHOLD_FOLDER_PREFIX, threshold));
        if(file.exists()){
            return file.getAbsolutePath();
        }
        try {
            FileUtils.forceMkdir(file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Should not happen, the directory wasn't created", e);
            throw new FolderCreationException("Could not create directory");
        }
    }
}
