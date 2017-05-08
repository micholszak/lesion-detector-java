package pl.olszak.michal.detector.fx;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
public class MainWindow extends Presentation {

    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    @FXML
    private StackPane databaseView;

    @FXML
    private StackPane emptyView;

    @FXML
    private JFXTextArea loggerOutput;

    public MainWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }

    @FXML
    public void initialize() {
        logger.info("Initialize logger and pin it to Output");
        screensConfiguration.displayLogMessages(loggerOutput);
    }

    @FXML
    public void loadDatabaseCreation() {
        logger.info("Load databases view");
        screensConfiguration.loadDatabaseWindow(databaseView);
    }

    public void loadSegmentation() {
        logger.info("Load segmentation view");
        screensConfiguration.loadEmptyWindow(emptyView);
    }


}
