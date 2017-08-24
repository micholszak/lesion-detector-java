package pl.olszak.michal.detector.fx.scenes.roc;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.ValidationFacade;
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
import pl.olszak.michal.detector.controller.RocController;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.CallbackProvider;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DialogUtils;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * @author molszak
 *         created on 11.07.2017.
 */


// TODO: 11.07.2017 stworzyć kontroler dla Roc, wydaje mi się, że rysowanie wykresów może być na osobnym oknie z bazy danych wyciągane
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RocWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(RocWindow.class);

    @FXML
    private JFXComboBox<ColorReduce> colorReductionCombo;
    @FXML
    private JFXTextField segmentationFilesFolderText;
    @FXML
    private JFXTextField maskResourcesFolderText;
    @FXML
    private JFXTextField rocFilesDestinationText;
    @FXML
    private JFXTextField databaseCollectionNameText;
    @FXML
    private JFXTextField thresholdsImage;

    private final CallbackProvider callbackProvider;
    private final RocWindowContext rocWindowContext;
    private final RocController rocController;

    public RocWindow(ScreensConfiguration screensConfiguration, CallbackProvider callbackProvider, RocWindowContext rocWindowContext, RocController rocController) {
        super(screensConfiguration);
        this.callbackProvider = callbackProvider;
        this.rocWindowContext = rocWindowContext;
        this.rocController = rocController;
    }

    @FXML
    public void initialize() {
        databaseCollectionNameText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                ValidationFacade.validate(databaseCollectionNameText);
            }
        });
        thresholdsImage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                ValidationFacade.validate(thresholdsImage);
            }
        });
        ObservableList<ColorReduce> reduction = FXCollections.observableArrayList(ColorReduce.values());
        colorReductionCombo.setItems(reduction);
        colorReductionCombo.setValue(rocWindowContext.getColorReduce());
        colorReductionCombo.setCellFactory(callbackProvider.provideComboBoxCallback());
        colorReductionCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                rocWindowContext.setColorReduce(newValue);
            }
        });

        segmentationFilesFolderText.setText(rocWindowContext.getSegmentationFilesFolder());
        maskResourcesFolderText.setText(rocWindowContext.getMaskResourcesFolder());
        rocFilesDestinationText.setText(rocWindowContext.getRocFilesDestinationText());
        databaseCollectionNameText.setText(rocWindowContext.getDatabaseCollectionName());
        thresholdsImage.setText(rocWindowContext.getImageTresholds());

        databaseCollectionNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, oldValue)) {
                rocWindowContext.setDatabaseCollectionName(newValue);
            }
        });
        thresholdsImage.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                rocWindowContext.setImageTresholds(newValue);
            }
        }));
    }

    @FXML
    public void openSegmentationFilesFolder() {
        logger.info("Open segmentation files folder");
        Optional<File> segmentationFolder = DialogUtils.openFolder("Segmentation folder", screensConfiguration.getStage());
        if (segmentationFolder.isPresent()) {
            File file = segmentationFolder.get();
            rocWindowContext.setSegmentationFilesFolder(file.getAbsolutePath());
            segmentationFilesFolderText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void openMaskResourcesFolder() {
        logger.info("Open mask resources folder");
        Optional<File> maskResourcesFolder = DialogUtils.openFolder("Mask resources folder", screensConfiguration.getStage());
        if (maskResourcesFolder.isPresent()) {
            File file = maskResourcesFolder.get();
            rocWindowContext.setMaskResourcesFolder(file.getAbsolutePath());
            maskResourcesFolderText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void openRocFilesDestination() {
        logger.info("Open roc files destination");
        Optional<File> rocDestination = DialogUtils.openFolder("Roc files destination", screensConfiguration.getStage());
        if (rocDestination.isPresent()) {
            File file = rocDestination.get();
            rocWindowContext.setRocFilesDestinationText(file.getAbsolutePath());
            rocFilesDestinationText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void startRoc() {
        if (!ValidationFacade.validate(databaseCollectionNameText) ||
                !ValidationFacade.validate(thresholdsImage) ||
                StringUtils.isEmpty(rocWindowContext.getMaskResourcesFolder()) ||
                StringUtils.isEmpty(rocWindowContext.getRocFilesDestinationText()) ||
                StringUtils.isEmpty(rocWindowContext.getSegmentationFilesFolder())) {
            logger.error("Could not process roc creation, some of the fields are empty");
            return;
        }

        logger.info("Starting roc");
        Observable.just(rocWindowContext)
                .subscribeOn(Schedulers.computation())
                .subscribe(rocController::createRocDatabase);
    }

}
