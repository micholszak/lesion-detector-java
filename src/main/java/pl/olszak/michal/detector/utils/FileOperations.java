package pl.olszak.michal.detector.utils;

import org.apache.commons.io.FileUtils;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.ImageContainer;

import java.io.File;
import java.util.Iterator;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
public final class FileOperations {

    private final String[] ACCEPTABLE_EXTENSIONS;

    public FileOperations() {
        ACCEPTABLE_EXTENSIONS = new String[]{
                "bmp",
                "png",
                "ppm"
        };
    }

    public ImageContainer create(String root, ImageType type) {
        final File rootDirectory = new File(root);
        final Iterator<File> iterator = FileUtils.iterateFiles(rootDirectory, ACCEPTABLE_EXTENSIONS, true);

        final ImageContainer container = new ImageContainer(type);

        iterator.forEachRemaining(file -> {
            if (file.isFile()) {
                container.addImage(file);
            }
        });

        return container;
    }


}
