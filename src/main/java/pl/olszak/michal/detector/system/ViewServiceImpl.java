package pl.olszak.michal.detector.system;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * @author pczajkowski
 *         created on 11.07.2017.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ViewServiceImpl implements ViewService {

    private final Logger logger = LoggerFactory.getLogger(ViewServiceImpl.class);

    private final ConfigurableApplicationContext context;

    @Autowired
    public ViewServiceImpl(ConfigurableApplicationContext context) {
        this.context = context;
    }

    private <T> Node get(FXMLLoader loader, Consumer<T> consumer) {
        loader.setControllerFactory(context::getBean);
        try {
            Parent parent = loader.load();

            T controller = loader.getController();
            if (consumer != null) {
                consumer.accept(controller);
            }
            return parent;
        } catch (IOException e) {
            logger.error("Blad ładowania FXML", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Node getNode(String fxml) {
        return get(new FXMLLoader(getClass().getResource(fxml)), null);
    }

    @Override
    public <T> Node getNode(String fxml, Consumer<T> consumer) {
        return get(new FXMLLoader(getClass().getResource(fxml)), consumer);
    }

    @Override
    public Node getNode(String fxml, ResourceBundle bundle) {
        return get(new FXMLLoader(getClass().getResource(fxml), bundle), null);
    }

    @Override
    public <T> Node getNode(String fxml, ResourceBundle bundle, Consumer<T> consumer) {
        return get(new FXMLLoader(getClass().getResource(fxml), bundle), consumer);
    }

    @Override
    public Parent getParent(String fxml) {
        return (Parent) getNode(fxml);
    }

    @Override
    public <T> Parent getParent(String fxml, Consumer<T> consumer) {
        return (Parent) getNode(fxml, consumer);
    }

    @Override
    public Parent getParent(String fxml, ResourceBundle bundle) {
        return (Parent) getNode(fxml, bundle);
    }

    @Override
    public <T> Parent getParent(String fxml, ResourceBundle bundle, Consumer<T> consumer) {
        return (Parent) getNode(fxml, bundle, consumer);
    }
}
