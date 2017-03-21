package pl.olszak.michal.detector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * @author molszak
 *         created on 21.03.2017.
 */
public class Main {

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        System.out.println(Core.VERSION);
        Mat mat = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
        System.out.println(mat);
    }

}
