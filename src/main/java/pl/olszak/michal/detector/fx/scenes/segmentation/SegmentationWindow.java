package pl.olszak.michal.detector.fx.scenes.segmentation;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.DialogUtils;

import java.io.File;
import java.util.Optional;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
public class SegmentationWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(SegmentationWindow.class);

    @FXML
    private JFXTextField segmentationResourcesText;
    @FXML
    private JFXTextField segmentationDestinationText;

    @Autowired
    private SegmentationWindowContext context;

    public SegmentationWindow(ScreensConfiguration screensConfiguration) {
        super(screensConfiguration);
    }

    @FXML
    public void initialize(){
        segmentationResourcesText.setText(context.getSegmentationResourcesFolder());
        segmentationDestinationText.setText(context.getSegmentationDestinationFolder());
    }

    @FXML
    public void onOpenSegmentationFolder(){
        logger.info("Open segmentation folder");
        Optional<File> segmenattionResource = DialogUtils.openFolder("Segmentation resources", screensConfiguration.getStage());
        if(segmenattionResource.isPresent()){
            File file = segmenattionResource.get();
            context.setSegmentationResourcesFolder(file.getAbsolutePath());
            segmentationResourcesText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void onChooseSegmentationDestination(){
        logger.info("Open segmentation destination");
        Optional<File> segmenattionDestination = DialogUtils.openFolder("Choose destination for segmentation", screensConfiguration.getStage());
        if(segmenattionDestination.isPresent()){
            File file = segmenattionDestination.get();
            context.setSegmentationDestinationFolder(file.getAbsolutePath());
            segmentationDestinationText.setText(file.getAbsolutePath());
        }
    }
}
