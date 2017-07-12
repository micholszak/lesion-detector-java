package pl.olszak.michal.detector.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.image.ImageContainer;

import java.io.File;
import java.util.Iterator;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
public final class ContainerOperations {

    public static final String BMP_EXTENSION = ".bmp";

    private final Logger logger = LoggerFactory.getLogger(ContainerOperations.class);

    private final String[] ACCEPTABLE_EXTENSIONS;

    public ContainerOperations() {
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
                logger.info(String.format("Added $1%s to container", file.getName()));
            }
        });

        return container;
    }


}
