package pl.olszak.michal.detector.fx.scenes;

import com.jfoenix.svg.SVGGlyph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
public class FileReaderWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(FileReaderWindow.class);

    public FileReaderWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("ZAładowanY");
    }


    @FXML
    public void openSegmentationFolder() {

    }

    @FXML
    public void openMaskFolder() {

    }
}
