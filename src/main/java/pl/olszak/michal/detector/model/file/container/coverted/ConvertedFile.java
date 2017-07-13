package pl.olszak.michal.detector.model.file.container.coverted;

import org.opencv.core.Mat;
import pl.olszak.michal.detector.core.converter.ImageConverter;
import pl.olszak.michal.detector.model.file.AbstractImageFile;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public abstract class ConvertedFile {

    protected final AbstractImageFile image;
    protected final ImageConverter converter;

    public ConvertedFile(AbstractImageFile image, ImageConverter converter) {
        this.image = image;
        this.converter = converter;
    }

    public AbstractImageFile getImage() {
        return image;
    }

    public abstract Mat getConverted();
}
