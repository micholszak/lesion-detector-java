package pl.olszak.michal.detector.model.file.container.coverted;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.core.converter.ImageConverter;
import pl.olszak.michal.detector.model.file.AbstractImageFile;

import java.util.Locale;

/**
 * @author molszak
 *         created on 13.07.2017.
 */
public class ColoredConvertedFile extends ConvertedFile {

    private static final Logger logger = LoggerFactory.getLogger(ColoredConvertedFile.class);

    ColoredConvertedFile(AbstractImageFile image, ImageConverter converter) {
        super(image, converter);
    }

    @Override
    public Mat getConverted() {
        Mat converted = converter.convert(image);

        logger.info(String.format(Locale.getDefault(), "Created colored image %s", image.getFileName()));
        return converted;
    }

    @Override
    public Mat getConverted(int threshold) {
        return getConverted();
    }
}
