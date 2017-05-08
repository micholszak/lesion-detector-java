package pl.olszak.michal.detector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;

import java.io.IOException;

/**
 * @author molszak
 *         created on 21.03.2017.
 */
@SpringBootApplication
public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static String[] savedArgs;
    private ConfigurableApplicationContext context;

    public static void main(String[] args) throws IOException {
        savedArgs = args;
        launch(Main.class, args);
    }

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(getClass(), savedArgs);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Platform.setImplicitExit(true);
            ScreensConfiguration screensConfiguration = context.getBean(ScreensConfiguration.class);
            screensConfiguration.setStage(primaryStage);
            screensConfiguration.show();
            screensConfiguration.loadMainWindow();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() throws Exception {
        context.stop();
    }
}
