package pl.olszak.michal.detector.model.file.container.coverted;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.core.converter.ImageConverter;
import pl.olszak.michal.detector.model.file.AbstractImageFile;

/**
 * @author molszak
 *         created on 13.07.2017.
 */
public class ThresholdConvertedFile extends ConvertedFile {

    private static final Logger logger = LoggerFactory.getLogger(ThresholdConvertedFile.class);

    private final int threshold;

    ThresholdConvertedFile(AbstractImageFile image, ImageConverter converter, int threshold) {
        super(image, converter);
        this.threshold = threshold;
    }

    @Override
    public Mat getConverted() {
        Mat conversion = converter.convert(image);
        Mat threshed = converter.threshold(conversion, threshold, false);

        conversion.release();

        logger.info(String.format("Created threshold image %s", image.getFileName()));
        return threshed;
    }

    @Override
    public Mat getConverted(int threshold) {
        Mat conversion = converter.convert(image);
        Mat threshed = converter.threshold(conversion, threshold, false);

        conversion.release();

        logger.info(String.format("Created threshold image %s", image.getFileName()));
        return threshed;
    }
}
