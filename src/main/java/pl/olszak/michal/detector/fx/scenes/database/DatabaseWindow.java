package pl.olszak.michal.detector.fx.scenes.database;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import pl.olszak.michal.detector.controller.creator.MapCreator;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.CallbackProvider;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DialogUtils;

import java.io.File;
import java.util.Optional;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DatabaseWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(DatabaseWindow.class);

    @FXML
    private JFXTextField maskResourcesText;
    @FXML
    private JFXTextField imageResourcesText;
    @FXML
    private JFXComboBox<ColorReduce> imageReductionCombo;

    private final MapCreator probabilityMapCreator;
    private final DatabaseWindowContext model;
    private final CallbackProvider callbackProvider;

    public DatabaseWindow(ScreensConfiguration screensConfiguration, MapCreator probabilityMapCreator, DatabaseWindowContext model, CallbackProvider callbackProvider) {
        super(screensConfiguration);
        this.probabilityMapCreator = probabilityMapCreator;
        this.model = model;
        this.callbackProvider = callbackProvider;
    }

    @FXML
    public void initialize() {
        maskResourcesText.setText(model.getMaskFolder());
        imageResourcesText.setText(model.getImageResourcesFolder());

        ObservableList<ColorReduce> reduction = FXCollections.observableArrayList(ColorReduce.values());
        imageReductionCombo.setItems(reduction);
        imageReductionCombo.setCellFactory(callbackProvider.provideComboBoxCallback());
        imageReductionCombo.setValue(model.getColorReduce());
        imageReductionCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                model.setColorReduce(newValue);
            }
        });
    }


    @FXML
    public void onOpenTrainResourcesFolder() {
        logger.info("Open training resources folder");
        Optional<File> imageResources = DialogUtils.openFolder("Training Resources Folder", screensConfiguration.getStage());
        if (imageResources.isPresent()) {
            File file = imageResources.get();
            model.setImageResourcesFolder(file.getAbsolutePath());
            imageResourcesText.setText(file.getAbsolutePath());
        }

    }

    @FXML
    public void onOpenMaskFolder() {
        logger.info("Open mask folder");
        Optional<File> maskResources = DialogUtils.openFolder("Mask Resources", screensConfiguration.getStage());
        if (maskResources.isPresent()) {
            File file = maskResources.get();
            model.setMaskFolder(file.getAbsolutePath());
            maskResourcesText.setText(file.getAbsolutePath());
        }
    }

    /**
     * Niestety nie jest to idiotoodporne, ale nie chce mi się robić dodatkowego ładowania
     */
    @FXML
    public void onProcessTraining() {
        if (StringUtils.isEmpty(model.getImageResourcesFolder()) ||
                StringUtils.isEmpty(model.getMaskFolder())) {
            logger.error("Could not process set, the folders are not chosen");
            return;
        }
        logger.info("Process training");
        Observable.just(model.getColorReduce())
                .subscribeOn(Schedulers.computation())
                .subscribe(action -> probabilityMapCreator.process(action, model));
    }
}
