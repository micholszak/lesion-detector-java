package pl.olszak.michal.detector.fx.scenes.database;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
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

    @Autowired
    private DatabaseWindowContext model;

    public DatabaseWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }


    @FXML
    public void initialize() {
        if (model.getMaskFolder().isPresent()) {
            maskResourcesText.setText(model.getMaskFolder().get());
        }
        if (model.getImageResourcesFolder().isPresent()) {
            imageResourcesText.setText(model.getImageResourcesFolder().get());
        }
    }


    @FXML
    public void onOpenResourcesFolder() {
        logger.info("Open segmentation folder");
        Optional<File> imageResources = DialogUtils.openFolder("Image Resources Folder", screensConfiguration.getStage());
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
}
