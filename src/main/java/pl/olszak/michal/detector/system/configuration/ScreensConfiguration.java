package pl.olszak.michal.detector.system.configuration;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pl.olszak.michal.detector.system.ViewService;
import pl.olszak.michal.detector.utils.TextAreaAppender;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
@Configuration
@Lazy
public class ScreensConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ScreensConfiguration.class);

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String STYLESHEET = "/main.css";

    private Stage stage;
    private StackPane root;
    private TextAreaAppender appender;

    @Autowired
    private ViewService viewService;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        appender = (TextAreaAppender) rootLogger.getAppender("TEXTAREA");

        root = new StackPane();
        stage.setTitle("Leasion-Detector");

        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setScene(scene);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);

        stage.setOnHiding(event -> System.exit(0));
        stage.show();
    }

    public void displayLogMessages(TextArea textArea) {
        appender.setTextArea(textArea);
    }

    public void loadDatabaseWindow(Pane pane) {
        pane.getChildren().setAll(viewService.getNode("/fxml/ui/DatabaseWindow.fxml"));
    }

    public void loadRocWindow(Pane pane) {
        pane.getChildren().setAll(viewService.getNode("/fxml/ui/RocWindow.fxml"));
    }

    public void loadMainWindow() {
        setNode(viewService.getNode("/fxml/MainWindow.fxml"));
    }

    public void loadSegmentationWindow(Pane pane) {
        pane.getChildren().setAll(viewService.getNode("/fxml/ui/SegmentationWindow.fxml"));
    }

    private void setNode(Node node) {
        root.getChildren().setAll(node);
    }
}
