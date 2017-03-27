package pl.olszak.michal.detector.model.file.container.coverted;

import org.opencv.core.Mat;
import pl.olszak.michal.detector.model.file.AbstractImageFile;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class ConvertedFile {

    private final AbstractImageFile image;
    private final Mat converted;

    public ConvertedFile(AbstractImageFile image, Mat converted) {
        this.image = image;
        this.converted = converted;
    }

    public AbstractImageFile getImage() {
        return image;
    }

    public Mat getConverted() {
        return converted;
    }
}
