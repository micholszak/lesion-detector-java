package pl.olszak.michal.detector.model.file.container.coverted;

import pl.olszak.michal.detector.core.converter.ImageConverter;
import pl.olszak.michal.detector.model.file.AbstractImageFile;

/**
 * @author molszak
 *         created on 13.07.2017.
 */
public class ConvertedFileFactory {

    public static ConvertedFile createColored(AbstractImageFile image, ImageConverter converter) {
        return new ColoredConvertedFile(image, converter);
    }

    public static ConvertedFile createThreshold(AbstractImageFile image, ImageConverter converter, int threshold, boolean constant) {
        if (constant) {
            return new MaskConvertedFile(image, converter);
        }
        return new ThresholdConvertedFile(image, converter, threshold);
    }

}
