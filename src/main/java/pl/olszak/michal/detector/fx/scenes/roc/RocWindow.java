package pl.olszak.michal.detector.fx.scenes.roc;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.olszak.michal.detector.controller.RocController;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.CallbackProvider;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DialogUtils;
import pl.olszak.michal.detector.utils.Strings;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * @author molszak
 *         created on 11.07.2017.
 */

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RocWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(RocWindow.class);

    @FXML
    private ComboBox<ColorReduce> colorReductionCombo;
    @FXML
    private TextField segmentationFilesFolderText;
    @FXML
    private TextField maskResourcesFolderText;
    @FXML
    private TextField rocFilesDestinationText;
    @FXML
    private TextField databaseCollectionNameText;
    @FXML
    private TextField thresholdsImage;

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
        if (canProcessDatabase()) {
            logger.info("Starting roc");
            Observable.just(rocWindowContext)
                    .subscribeOn(Schedulers.computation())
                    .subscribe(rocController::createRocDatabase);
            logger.error("Could not process roc creation, some of the fields are empty");
        }else logger.error("Could not process roc creation, some of the fields are empty");
    }

    private boolean canProcessDatabase(){
        String collectionName = databaseCollectionNameText.getText();
        String thresholdsImageOption = thresholdsImage.getText();

        return Strings.areNotEmpty(collectionName,
                thresholdsImageOption,
                rocWindowContext.getMaskResourcesFolder(),
                rocWindowContext.getRocFilesDestinationText(),
                rocWindowContext.getSegmentationFilesFolder());
    }
}
