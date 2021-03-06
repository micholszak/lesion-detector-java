package pl.olszak.michal.detector.fx.scenes.segmentation;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import pl.olszak.michal.detector.controller.SegmentationController;
import pl.olszak.michal.detector.fx.Presentation;
import pl.olszak.michal.detector.system.configuration.ScreensConfiguration;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DialogUtils;

import java.io.File;
import java.util.Optional;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SegmentationWindow extends Presentation {

    private final Logger logger = LoggerFactory.getLogger(SegmentationWindow.class);

    @FXML
    private TextField segmentationResourcesText;
    @FXML
    private TextField segmentationDestinationText;

    private final SegmentationWindowContext context;
    private final SegmentationController segmentationController;

    public SegmentationWindow(ScreensConfiguration screensConfiguration, SegmentationWindowContext context, SegmentationController segmentationController) {
        super(screensConfiguration);
        this.context = context;
        this.segmentationController = segmentationController;
    }

    @FXML
    public void initialize() {
        segmentationResourcesText.setText(context.getSegmentationResourcesFolder());
        segmentationDestinationText.setText(context.getSegmentationDestinationFolder());
    }

    @FXML
    public void onOpenSegmentationFolder() {
        logger.info("Open segmentation folder");
        Optional<File> segmenattionResource = DialogUtils.openFolder("Segmentation resources", screensConfiguration.getStage());
        if (segmenattionResource.isPresent()) {
            File file = segmenattionResource.get();
            context.setSegmentationResourcesFolder(file.getAbsolutePath());
            segmentationResourcesText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void onChooseSegmentationDestination() {
        logger.info("Open segmentation destination");
        Optional<File> segmenationDestination = DialogUtils.openFolder("Choose destination for segmentation", screensConfiguration.getStage());
        if (segmenationDestination.isPresent()) {
            File file = segmenationDestination.get();
            context.setSegmentationDestinationFolder(file.getAbsolutePath());
            segmentationDestinationText.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void processSegmentation() {
        logger.info("Segmenattion process started");
        if (StringUtils.isEmpty(context.getSegmentationDestinationFolder()) ||
                StringUtils.isEmpty(context.getSegmentationResourcesFolder())) {
            logger.error("Could not start the segmentation");
            return;
        }

        Observable.fromArray(ColorReduce.values())
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(action -> segmentationController.createSegmentedImages(action, context));
    }
}
