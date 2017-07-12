package pl.olszak.michal.detector.controller;

import io.reactivex.Flowable;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.core.converter.ConvertedContainerCreator;
import pl.olszak.michal.detector.core.database.DatabaseFacade;
import pl.olszak.michal.detector.exception.FolderCreationException;
import pl.olszak.michal.detector.fx.scenes.roc.RocWindowContext;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;
import pl.olszak.michal.detector.model.file.container.image.ImageContainer;
import pl.olszak.michal.detector.utils.ContainerOperations;
import pl.olszak.michal.detector.utils.FileOperationsUtils;

import java.util.Set;

/**
 * @author molszak
 *         created on 12.07.2017.
 */
public class RocController {

    private final Logger logger = LoggerFactory.getLogger(RocController.class);

    private final ContainerOperations operations;
    private final ConvertedContainerCreator creator;
    private final DatabaseFacade databaseFacade;

    private static final int MAX_TRESH = 256;

    public RocController(ContainerOperations operations, ConvertedContainerCreator creator, DatabaseFacade databaseFacade) {
        this.operations = operations;
        this.creator = creator;
        this.databaseFacade = databaseFacade;
    }

    public void createRocDatabase(final RocWindowContext context) {
        logger.info("Started Roc Database creation process");
        if (databaseFacade.probabilityDatabaseExists()) {
            create(context);
        } else {
            logger.info("Firstly process the probability map before creating Roc");
        }

        logger.info("Roc process finished");
    }

    private void create(final RocWindowContext context) {
        ImageContainer segmentationFilesContainer = operations.create(context.getSegmentationFilesFolder(), ImageType.GRAYSCALE_SEGMENTED);
        ImageContainer maskFiles = operations.create(context.getMaskResourcesFolder(), ImageType.GRAYSCALE_MASK);

        ConvertedContainer maskConverted = creator.createThresholds(maskFiles.getImages(), 128, true);
        Set<Integer> visualizationThresholds = context.getTresholds();

        for (int threshold = 0; threshold < MAX_TRESH; threshold++) {
            logger.info("Creating Roc for threshold " + threshold);

            ConvertedContainer segmentedConverted = creator.createThresholds(segmentationFilesContainer.getImages(), threshold, false);

            if (visualizationThresholds.contains(threshold)) {
                try {
                    String destination = FileOperationsUtils.createDestinationFolder(threshold, context.getRocFilesDestinationText());
                    processVisualizationContainer(destination, maskConverted, segmentedConverted, threshold);
                } catch (FolderCreationException e) {
                    logger.error(e.getMessage());
                    logger.info("Processing with regular creation");
                    processContainer(maskConverted, segmentedConverted, threshold);
                }
            } else {
                processContainer(maskConverted, segmentedConverted, threshold);
            }

        }
    }

    private void processContainer(final ConvertedContainer maskConverted, final ConvertedContainer segmentedConverted, final int threshold) {
        Flowable.fromIterable(segmentedConverted.getConvertedFiles().entrySet())
                .filter(entry -> maskConverted.getConvertedFiles().containsKey(entry.getKey()))
                .forEach(entry -> processSingle(entry.getKey(), maskConverted.getConvertedFiles().get(entry.getKey()), entry.getValue(), threshold));
    }

    private void processVisualizationContainer(final String destination, final ConvertedContainer maskConverted, final ConvertedContainer segmentedConverted, final int threshold) {
        Flowable.fromIterable(segmentedConverted.getConvertedFiles().entrySet())
                .filter(entry -> maskConverted.getConvertedFiles().containsKey(entry.getKey()))
                .forEach(entry -> processSingleVisualization(destination, entry.getKey(), maskConverted.getConvertedFiles().get(entry.getKey()), entry.getValue(), threshold));
    }

    // TODO: 12.07.2017 dokończyć
    private void processSingleVisualization(final String destination, final String filename, final ConvertedFile convertedMask, final ConvertedFile convertedSegmentation, final int threshold) {
        Mat mask = convertedMask.getConverted();
        Mat segmentation = convertedSegmentation.getConverted();

        int falsePositive = 0;
        int falseNegative = 0;
        int truePositive = 0;
        int trueNegative = 0;

        byte imageBytes[] = new byte[(int) (mask.size().height * mask.size().width * CvType.CV_8SC(3))];

        for(int row = 0; row < mask.height(); row ++){
            for(int col = 0; col < mask.width(); col ++){
            }
        }
    }

    private void processSingle(final String fileName, final ConvertedFile convertedMask, final ConvertedFile convertedSegmentation, final int threshold) {

    }


}
