package pl.olszak.michal.detector.model.files;

import java.io.File;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public class MaskImageFile extends AbstractImageFile {

    public MaskImageFile(File file) {
        super(file);
    }

    @Override
    public ImageType getImageType() {
        return ImageType.GRAYSCALE_MASK;
    }
}
