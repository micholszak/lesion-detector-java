package pl.olszak.michal.detector.model.file.container.image;

import pl.olszak.michal.detector.model.file.AbstractImageFile;
import pl.olszak.michal.detector.model.file.ColoredImageFile;
import pl.olszak.michal.detector.model.file.GrayscaleImageFile;
import pl.olszak.michal.detector.model.file.MaskImageFile;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
public final class ImageContainerFactory {

    public static AbstractImageFile create(ImageContainerContext context) {
        switch (context.getType()) {
            case GRAYSCALE_MASK:
                return new MaskImageFile(context.getImage());
            case GRAYSCALE_SEGMENTED:
                return new GrayscaleImageFile(context.getImage());
            case COLORED:
            default:
                return new ColoredImageFile(context.getImage());
        }
    }


}
