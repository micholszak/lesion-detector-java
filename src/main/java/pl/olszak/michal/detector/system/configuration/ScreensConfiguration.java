package pl.olszak.michal.detector.system.configuration;

import com.jfoenix.controls.JFXDecorator;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.fx.scenes.FileReaderWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
@Configuration
@Lazy
// TODO: 21.04.2017 dodać datafx może
public class ScreensConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ScreensConfiguration.class);

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    // TODO: 21.04.2017 wymienić stylesheet na coś fajnego
    private static final String STYLESHEET = "main.css";

    private Stage stage;
    private Scene scene;
    private StackPane root;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        root = new StackPane();
        root.getStyleClass().add("main-window");
        stage.setTitle("Leasion-Detector");

        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.setCustomMaximize(true);

        scene = new Scene(decorator, WIDTH, HEIGHT);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        /*stylesheets.addAll(getClass().getResource("/css/jfoenix-fonts.css").toExternalForm(),
                getClass().getResource("/css/jfoenix-design.css").toExternalForm());*/

        stage.setScene(scene);
        stage.setMinHeight(HEIGHT);
        stage.setMinWidth(WIDTH);

        stage.setOnHiding(event -> System.exit(0));
        stage.show();
    }

    public void loadFileReaderWindow() {
        setNode(getNode(fileReaderWindow(), getClass().getResource("/fxml/FileReaderWindow.fxml")));
    }

    @Bean
    @Scope("prototype")
    FileReaderWindow fileReaderWindow() {
        return new FileReaderWindow(this);
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
