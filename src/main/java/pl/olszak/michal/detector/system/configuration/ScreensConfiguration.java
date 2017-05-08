package pl.olszak.michal.detector.system.configuration;

import com.jfoenix.controls.JFXDecorator;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import pl.olszak.michal.detector.fx.MainWindow;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.fx.scenes.database.DatabaseWindow;
import pl.olszak.michal.detector.fx.scenes.EmptyTestWindow;
import pl.olszak.michal.detector.utils.TextAreaAppender;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    // TODO: 21.04.2017 wymienić stylesheet na coś fajnego
    private static final String STYLESHEET = "/main.css";

    private Stage stage;
    private StackPane root;
    private TextAreaAppender appender;

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
        root.getStyleClass().add("main-window");
        stage.setTitle("Leasion-Detector");

        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);

        Scene scene = new Scene(decorator, WIDTH, HEIGHT);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.add(getClass().getResource(STYLESHEET).toExternalForm());

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
        pane.getChildren().setAll(getNode(databaseWindow(), getClass().getResource("/fxml/DatabaseWindow.fxml")));
    }

    public void loadEmptyWindow(Pane pane) {
        pane.getChildren().setAll(getNode(emptyTestWindow(), getClass().getResource("/fxml/EmptyTestWindow.fxml")));
    }

    public void loadMainWindow() {
        setNode(getNode(mainWindow(), getClass().getResource("/fxml/MainWindow.fxml")));
    }

    @Bean
    @Scope("singleton")
    DatabaseWindow databaseWindow() {
        return new DatabaseWindow(this);
    }

    @Bean
    @Scope("prototype")
    MainWindow mainWindow() {
        return new MainWindow(this);
    }

    @Bean
    @Scope("prototype")
    EmptyTestWindow emptyTestWindow() {
        return new EmptyTestWindow(this);
    }

    private void setNode(Node node) {
        root.getChildren().setAll(node);
    }

    private Node getNode(final Presentation presentation, URL location) {
        FXMLLoader loader = new FXMLLoader(location, ResourceBundle.getBundle("lang_pl"));
        loader.setControllerFactory(param -> presentation);

        try {
            return (Node) loader.load();
        } catch (IOException e) {
            logger.error("Error retrieving node", e);
            return null;
        }
    }
}
