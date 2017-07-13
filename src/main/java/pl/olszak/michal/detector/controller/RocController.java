package pl.olszak.michal.detector.controller;

import io.reactivex.Flowable;
import io.reactivex.Single;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.olszak.michal.detector.core.converter.ConvertedContainerCreator;
import pl.olszak.michal.detector.core.database.DatabaseFacade;
import pl.olszak.michal.detector.exception.FolderCreationException;
import pl.olszak.michal.detector.fx.scenes.roc.RocWindowContext;
import pl.olszak.michal.detector.model.data.RocDataSet;
import pl.olszak.michal.detector.model.data.RocModel;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;
import pl.olszak.michal.detector.model.file.container.image.ImageContainer;
import pl.olszak.michal.detector.utils.ContainerOperations;
import pl.olszak.michal.detector.utils.FileOperationsUtils;
import pl.olszak.michal.detector.utils.Operations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
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

    private static final int MAX_THRESH = 256;

    public RocController(ContainerOperations operations, ConvertedContainerCreator creator, DatabaseFacade databaseFacade) {
        this.operations = operations;
        this.creator = creator;
        this.databaseFacade = databaseFacade;
    }

    public void createRocDatabase(final RocWindowContext context) {
        logger.info("Started Roc Database creation process");
        if (databaseFacade.probabilityDatabaseExists() && !databaseFacade.rocExists(context.getDatabaseCollectionName())) {
            create(context);
        } else {
            logger.info("Firstly process the probability map before creating Roc");
            logger.info("Be sure that collection name is unique");
        }
    }

    private void create(final RocWindowContext context) {
        ImageContainer segmentationFilesContainer = operations.create(context.getSegmentationFilesFolder(), ImageType.GRAYSCALE_SEGMENTED);
        ImageContainer maskFiles = operations.create(context.getMaskResourcesFolder(), ImageType.GRAYSCALE_MASK);

        ConvertedContainer maskConverted = creator.createThresholds(maskFiles.getImages(), 128, true);
        Set<Integer> visualizationThresholds = context.getTresholds();

        Set<RocModel> rocModels = new HashSet<>();

        for (int threshold = 0; threshold < MAX_THRESH; threshold++) {
            logger.info("Creating Roc for threshold " + threshold);

            ConvertedContainer segmentedConverted = creator.createThresholds(segmentationFilesContainer.getImages(), threshold, false);

            if (visualizationThresholds.contains(threshold)) {
                try {
                    String destination = FileOperationsUtils.createDestinationFolder(threshold, context.getRocFilesDestinationText());
                    processVisualizationContainer(destination, maskConverted, segmentedConverted, threshold)
                            .subscribe(rocModels::addAll);
                } catch (FolderCreationException e) {
                    logger.error(e.getMessage());
                    logger.info("Processing with regular creation");
                    processContainer(maskConverted, segmentedConverted, threshold)
                            .subscribe(rocModels::addAll);
                }
            } else {
                processContainer(maskConverted, segmentedConverted, threshold)
                        .subscribe(rocModels::addAll);
            }
        }

        RocDataSet rocDataSet = new RocDataSet(context.getDatabaseCollectionName(), context.getColorReduce(), rocModels);
        databaseFacade.insert(rocDataSet);
        logger.info("Roc process finished");
    }

    private Single<HashSet<RocModel>> processContainer(final ConvertedContainer maskConverted, final ConvertedContainer segmentedConverted, final int threshold) {
        return Flowable.fromIterable(segmentedConverted.getConvertedFiles().entrySet())
                .filter(entry -> maskConverted.getConvertedFiles().containsKey(entry.getKey()))
                .map(entry -> processSingle(entry.getKey(), maskConverted.getConvertedFiles().get(entry.getKey()), entry.getValue(), threshold))
                .collectInto(new HashSet<RocModel>(), Set::add);
    }

    private Single<HashSet<RocModel>> processVisualizationContainer(final String destination, final ConvertedContainer maskConverted, final ConvertedContainer segmentedConverted, final int threshold) {
        return Flowable.fromIterable(segmentedConverted.getConvertedFiles().entrySet())
                .filter(entry -> maskConverted.getConvertedFiles().containsKey(entry.getKey()))
                .map(entry -> processSingleVisualization(destination, entry.getKey(), maskConverted.getConvertedFiles().get(entry.getKey()), entry.getValue(), threshold))
                .collectInto(new HashSet<RocModel>(), Set::add);
    }

    private RocModel processSingleVisualization(final String destination, final String filename, final ConvertedFile convertedMask, final ConvertedFile convertedSegmentation, final int threshold) {
        Mat mask = convertedMask.getConverted();
        Mat segmentation = convertedSegmentation.getConverted();

        logger.info(String.format(Locale.getDefault(), "Processing visualization for %s threshold %d", filename, threshold));

        int falsePositive = 0;
        int falseNegative = 0;
        int truePositive = 0;
        int trueNegative = 0;

        createSegmentationResultAndMaskImages(segmentation, mask, filename, destination);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(mask.height() * mask.width() * CvType.CV_8SC(3))) {
            for (int row = 0; row < mask.height(); row++) {
                for (int col = 0; col < mask.width(); col++) {
                    final byte[] segBytes = new byte[segmentation.channels()];
                    final byte[] maskBytes = new byte[mask.channels()];

                    mask.get(row, col, maskBytes);
                    segmentation.get(row, col, segBytes);

                    if (segBytes[0] == 0 && maskBytes[0] == segBytes[0]) {
                        trueNegative++;
                        outputStream.write(Operations.createColor(Operations.RocColor.BLACK));
                    } else if (segBytes[0] == 1 && maskBytes[0] == segBytes[0]) {
                        truePositive++;
                        outputStream.write(Operations.createColor(Operations.RocColor.WHITE));
                    } else if (segBytes[0] == 0) {
                        falseNegative++;
                        outputStream.write(Operations.createColor(Operations.RocColor.BLUE));
                    } else if (segBytes[0] == 1) {
                        falsePositive++;
                        outputStream.write(Operations.createColor(Operations.RocColor.RED));
                    }
                }
            }

            Mat mat = new Mat(segmentation.size(), CvType.CV_8UC(3));
            mat.put(0, 0, outputStream.toByteArray());

            File file = new File(destination, filename + ContainerOperations.BMP_EXTENSION);
            Imgcodecs.imwrite(file.getAbsolutePath(), mat);

            mat.release();
        } catch (IOException e) {
            //omit
        }

        mask.release();
        segmentation.release();

        return new RocModel(filename,
                threshold,
                falsePositive,
                falseNegative,
                truePositive,
                trueNegative,
                segmentation.width() * segmentation.height());

    }

    private RocModel processSingle(final String filename, final ConvertedFile convertedMask, final ConvertedFile convertedSegmentation, final int threshold) {
        Mat mask = convertedMask.getConverted();
        Mat segmentation = convertedSegmentation.getConverted();

        logger.info(String.format("Processing single for %s threshold %d", filename, threshold));

        int falsePositive = 0;
        int falseNegative = 0;
        int truePositive = 0;
        int trueNegative = 0;

        for (int row = 0; row < mask.height(); row++) {
            for (int col = 0; col < mask.width(); col++) {
                final byte[] segBytes = new byte[segmentation.channels()];
                final byte[] maskBytes = new byte[mask.channels()];

                mask.get(row, col, maskBytes);
                segmentation.get(row, col, segBytes);

                if (segBytes[0] == 0 && maskBytes[0] == segBytes[0]) {
                    trueNegative++;
                } else if (segBytes[0] == 1 && maskBytes[0] == segBytes[0]) {
                    truePositive++;
                } else if (segBytes[0] == 0) {
                    falseNegative++;
                } else if (segBytes[0] == 1) {
                    falsePositive++;
                }
            }
        }

        mask.release();
        segmentation.release();

        return new RocModel(filename,
                threshold,
                falsePositive,
                falseNegative,
                truePositive,
                trueNegative,
                segmentation.width() * segmentation.height());
    }

    // TODO: 13.07.2017 może zrobić z tego converted file ?
    private static void createSegmentationResultAndMaskImages(Mat segmentation, Mat mask, String filename, String destination) {
        File segFile = new File(destination, filename + "-seg" + ContainerOperations.BMP_EXTENSION);
        Mat segDisplay = new Mat(segmentation.size(), CvType.CV_8U);
        Imgproc.threshold(segmentation, segDisplay, 0, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(segFile.getAbsolutePath(), segDisplay);

        segDisplay.release();

        File maskFile = new File(destination, filename + "-mask" + ContainerOperations.BMP_EXTENSION);
        Mat maskDisplay = new Mat(mask.size(), CvType.CV_8U);
        Imgproc.threshold(mask, maskDisplay, 0, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(maskFile.getAbsolutePath(), maskDisplay);

        maskDisplay.release();
    }


}
