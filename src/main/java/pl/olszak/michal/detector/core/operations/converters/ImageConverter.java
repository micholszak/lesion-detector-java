package pl.olszak.michal.detector.core.operations.converters;

import org.opencv.core.Mat;
import pl.olszak.michal.detector.model.files.AbstractImageFile;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public interface ImageConverter {

    Mat convert(AbstractImageFile img);

}
