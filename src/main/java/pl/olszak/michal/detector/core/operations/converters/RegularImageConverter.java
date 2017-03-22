package pl.olszak.michal.detector.core.operations.converters;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import pl.olszak.michal.detector.model.files.AbstractImageFile;
import pl.olszak.michal.detector.model.files.ImageType;

import java.io.File;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public class RegularImageConverter implements ImageConverter {

    @Override
    public Mat convert(AbstractImageFile img) {
        File file = img.getFile();
        ImageType type = img.getImageType();

        Mat imageMat = Imgcodecs.imread(file.getPath(), type.getLoadType());
        return imageMat;
    }
}
