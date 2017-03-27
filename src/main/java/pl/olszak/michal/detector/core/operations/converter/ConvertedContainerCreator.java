package pl.olszak.michal.detector.core.operations.converter;

import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import pl.olszak.michal.detector.model.file.AbstractImageFile;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;

import java.util.List;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class ConvertedContainerCreator {

    private final ImageConverter converter;

    public ConvertedContainerCreator(ImageConverter converter) {
        this.converter = converter;
    }

    public ConvertedContainer createColoredContainer(final List<AbstractImageFile> files) {
        final ConvertedContainer container = new ConvertedContainer();

        files.forEach(image -> {
            Mat conversion = converter.convert(image);

            ConvertedFile converted = new ConvertedFile(image, conversion);
            container.put(FilenameUtils.getBaseName(image.getFileName()), converted);
        });

        return container;
    }

    public ConvertedContainer createThresholded(final List<AbstractImageFile> files, final int threshold, final boolean constantThreshold) {
        final ConvertedContainer container = new ConvertedContainer();

        files.forEach(image -> {
            Mat conversion = converter.convert(image);
            Mat thresholded = converter.threshold(conversion, threshold, constantThreshold);

            ConvertedFile converted = new ConvertedFile(image, thresholded);
            container.put(FilenameUtils.getBaseName(image.getFileName()), converted);
        });

        return container;
    }
}

