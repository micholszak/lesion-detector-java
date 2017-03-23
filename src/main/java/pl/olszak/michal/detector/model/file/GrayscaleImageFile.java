package pl.olszak.michal.detector.model.file;

import java.io.File;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public class GrayscaleImageFile extends AbstractImageFile {

    public GrayscaleImageFile(File file) {
        super(file);
    }

    @Override
    public ImageType getImageType() {
        return ImageType.GRAYSCALE_SEGMENTED;
    }
}
