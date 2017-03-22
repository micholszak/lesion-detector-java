package pl.olszak.michal.detector.system.configuration;

import org.opencv.core.Core;
import org.springframework.context.annotation.Configuration;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
@Configuration
public class OpenCvConfiguration {

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

}
