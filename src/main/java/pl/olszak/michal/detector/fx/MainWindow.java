package pl.olszak.michal.detector.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainWindow extends Presentation {

    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab databaseTab;

    @FXML
    private Tab segmentationTab;

    @FXML
    private Tab rocTab;

    @FXML
    private StackPane databaseView;

    @FXML
    private StackPane segmentationView;

    @FXML
    private StackPane rocView;

    @FXML
    private TextArea loggerOutput;

    public MainWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }

    @FXML
    public void initialize() {
        logger.info("Initialize logger and pin it to Output");
        screensConfiguration.displayLogMessages(loggerOutput);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == databaseTab) {
                loadDatabaseCreation();
            } else if (newTab == segmentationTab) {
                loadSegmentation();
            } else if (newTab == rocTab) {
                loadRoc();
            }
        });
        loadDatabaseCreation();
    }

    private void loadRoc() {
        logger.info("Load roc view");
        screensConfiguration.loadRocWindow(rocView);
    }

    private void loadDatabaseCreation() {
        logger.info("Load databases view");
        screensConfiguration.loadDatabaseWindow(databaseView);
    }

    private void loadSegmentation() {
        logger.info("Load segmentation view");
        screensConfiguration.loadSegmentationWindow(segmentationView);
    }


}
