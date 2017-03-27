package pl.olszak.michal.detector.model.file.container.image;

import org.opencv.core.Mat;
import pl.olszak.michal.detector.model.file.ImageType;

import java.io.File;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
public class ImageContainerContext {

    private final ImageType type;
    private final File image;

    public ImageContainerContext(ImageType type, File image) {
        this.type = type;
        this.image = image;
    }

    public ImageType getType() {
        return type;
    }

    public File getImage() {
        return image;
    }
}
