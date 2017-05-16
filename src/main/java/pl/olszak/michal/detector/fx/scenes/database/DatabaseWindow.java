package pl.olszak.michal.detector.fx.scenes.database;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.olszak.michal.detector.core.operations.controller.ProbabilityMapCreatorController;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DialogUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author molszak
 *         created on 21.04.2017.
 */
public class DatabaseWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(DatabaseWindow.class);

    @FXML
    private JFXTextField maskResourcesText;
    @FXML
    private JFXTextField imageResourcesText;
    @FXML
    private JFXSpinner spinner;

    @Autowired
    ProbabilityMapCreatorController probabilityMapCreatorController;
    @Autowired
    private DatabaseWindowContext model;

    public DatabaseWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }


    @FXML
    public void initialize() {
        maskResourcesText.setText(model.getMaskFolder());
        imageResourcesText.setText(model.getImageResourcesFolder());
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

    @FXML
    public void onProcessTraining() {
        logger.info("Process training");
        Optional<File> jsonDatabaseFolder = DialogUtils.openFolder("Choose folder, to store json database", screensConfiguration.getStage());
        if (!jsonDatabaseFolder.isPresent()) {
            logger.error("Didn't choose any folder, abort");
            return;
        }
        File file = jsonDatabaseFolder.get();
        model.setJsonDatabaseFolder(file.getAbsolutePath());
        spinner.setVisible(true);
        Disposable disposable =  Observable.just(true)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe((action) -> probabilityMapCreatorController.process(ColorReduce.BINS_PER_CHANNEL_256, model));
    }
}
