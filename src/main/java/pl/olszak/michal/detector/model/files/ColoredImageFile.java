package pl.olszak.michal.detector.model.files;

import java.io.File;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public class ColoredImageFile extends AbstractImageFile {

    public ColoredImageFile(File file) {
        super(file);
    }

    @Override
    public ImageType getImageType() {
        return ImageType.COLORED;
    }
}
