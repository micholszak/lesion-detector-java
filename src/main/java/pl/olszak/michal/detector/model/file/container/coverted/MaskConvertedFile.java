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
public class MaskConvertedFile extends ConvertedFile {

    private static final Logger logger = LoggerFactory.getLogger(MaskConvertedFile.class);

    MaskConvertedFile(AbstractImageFile image, ImageConverter converter) {
        super(image, converter);
    }

    @Override
    public Mat getConverted() {
        Mat converted = converter.convert(image);
        Mat mask = converter.threshold(converted, 0, true);

        converted.release();
        logger.info(String.format(Locale.getDefault(), "Created mask image %s", image.getFileName()));
        return mask;
    }

    @Override
    public Mat getConverted(int threshold) {
        return getConverted();
    }
}
