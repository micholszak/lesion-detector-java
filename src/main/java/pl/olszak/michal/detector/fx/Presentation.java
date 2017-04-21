package pl.olszak.michal.detector.fx;

import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
public abstract class Presentation {

    protected final ScreensConfiguration screensConfiguration;

    protected Presentation(ScreensConfiguration screensConfiguration) {
        this.screensConfiguration = screensConfiguration;
    }
}
