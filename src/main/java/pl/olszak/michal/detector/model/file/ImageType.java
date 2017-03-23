package pl.olszak.michal.detector.model.file;

import org.opencv.core.CvType;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public enum ImageType {

    GRAYSCALE_SEGMENTED(CvType.CV_8U, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE, false),

    GRAYSCALE_MASK(CvType.CV_8U, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE, true),

    COLORED(CvType.CV_8U, Imgcodecs.CV_LOAD_IMAGE_COLOR, false);

    private final int cvType;
    private final int loadType;
    private final boolean constantThreshold;

    ImageType(int cvType, int loadType, boolean constantThreshold) {
        this.cvType = cvType;
        this.loadType = loadType;
        this.constantThreshold = constantThreshold;
    }

    public int getCvType() {
        return cvType;
    }

    public int getLoadType() {
        return loadType;
    }

    public boolean isConstantThreshold() {
        return constantThreshold;
    }
}
