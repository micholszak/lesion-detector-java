package pl.olszak.michal.detector.system;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * @author pczajkowski
 *         created on 11.07.2017.
 */
public interface ViewService {

    Node getNode(String fxml);

    <T> Node getNode(String fxml, Consumer<T> consumer);

    Node getNode(String fxml, ResourceBundle resourceBundle);

    <T> Node getNode(String fxml, ResourceBundle resourceBundle, Consumer<T> consumer);

    Parent getParent(String fxml);

    <T> Parent getParent(String fxml, Consumer<T> consumer);

    Parent getParent(String fxml, ResourceBundle resourceBundle);

    <T> Parent getParent(String fxml, ResourceBundle resourceBundle, Consumer<T> consumer);
}
