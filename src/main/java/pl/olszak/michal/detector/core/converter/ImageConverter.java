package pl.olszak.michal.detector.core.converter;

import org.opencv.core.Mat;
import pl.olszak.michal.detector.model.file.AbstractImageFile;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public interface ImageConverter {

    Mat convert(AbstractImageFile img);

    Mat threshold(Mat image, int thresh, boolean constant);

}
