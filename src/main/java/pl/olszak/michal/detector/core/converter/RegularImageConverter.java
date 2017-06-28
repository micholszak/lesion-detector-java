package pl.olszak.michal.detector.core.converter;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import pl.olszak.michal.detector.model.file.AbstractImageFile;
import pl.olszak.michal.detector.model.file.ImageType;

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

        return Imgcodecs.imread(file.getPath(), type.getLoadType());
    }

    @Override
    public Mat threshold(Mat image, int thresh, boolean constant) {
        Mat thresholded = new Mat(image.height(), image.width(), image.type());

        if (constant) {
            Imgproc.threshold(image, thresholded, 0, 1, Imgproc.THRESH_BINARY);
        } else {
            Imgproc.threshold(image, thresholded, thresh, 1, Imgproc.THRESH_BINARY);
        }

        return thresholded;
    }
}
