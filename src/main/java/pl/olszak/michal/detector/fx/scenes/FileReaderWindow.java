package pl.olszak.michal.detector.fx.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
public class FileReaderWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(FileReaderWindow.class);

    @FXML
    private TextArea loggerOutput;

    public FileReaderWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }


    @FXML
    public void initialize() {
        screensConfiguration.displayLogMessages(loggerOutput);
    }


    @FXML
    public void openSegmentationFolder() {
        logger.info("Click");
    }

    @FXML
    public void openMaskFolder() {
        logger.info("Ciekawe");
    }
}
