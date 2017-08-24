package pl.olszak.michal.detector.controller;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
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
import java.util.Locale;
import java.util.Set;

/**
 * @author molszak
 * created on 12.07.2017.
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
        if (databaseFacade.probabilityDatabaseExists() && !databaseFacade.rocExists(context.getDatabaseCollectionName(), context.getColorReduce())) {
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

        ConvertedContainer segmentedConverted = creator.createThresholds(segmentationFilesContainer.getImages(), 1, false);

        processContainer(context, maskConverted, segmentedConverted);
        logger.info("Roc process finished");
    }

    private Disposable processContainer(RocWindowContext context, final ConvertedContainer maskConverted, final ConvertedContainer segmentedConverted) {
        return Flowable.fromIterable(segmentedConverted.getConvertedFiles().entrySet())
                .filter(entry -> maskConverted.getConvertedFiles().containsKey(entry.getKey()))
                .subscribe(entry -> processSingle(context, entry.getKey(), maskConverted.getConvertedFiles().get(entry.getKey()), entry.getValue()));
    }

    private void processSingle(RocWindowContext context, final String filename, final ConvertedFile convertedMask, final ConvertedFile convertedSegmentation) {
        Mat mask = convertedMask.getConverted();

        Set<Integer> visualization = context.getTresholds();

        for (int threshold = 0; threshold < MAX_THRESH; threshold++) {
            if (visualization.contains(threshold)) {

                logger.info(String.format(Locale.getDefault(), "Processing visualization for %s threshold %d", filename, threshold));

                try {
                    Mat segmentation = convertedSegmentation.getConverted(threshold);
                    String destination = FileOperationsUtils.createDestinationFolder(threshold, context.getRocFilesDestinationText());
                    RocModel model = createVisualization(destination, mask, segmentation, filename, threshold, context);
                    databaseFacade.insert(model);

                    segmentation.release();
                } catch (FolderCreationException e) {
                    logger.info("Could not create folder, processing regular");
                    logger.info(String.format(Locale.getDefault(), "Processing single for %s threshold %d", filename, threshold));

                    Mat segmentation = convertedSegmentation.getConverted(threshold);

                    RocModel model = createWithoutVisualisation(mask, segmentation, filename, threshold, context);

                    databaseFacade.insert(model);
                    segmentation.release();

                }
            } else {
                logger.info(String.format(Locale.getDefault(), "Processing single for %s threshold %d", filename, threshold));

                Mat segmentation = convertedSegmentation.getConverted(threshold);

                RocModel model = createWithoutVisualisation(mask, segmentation, filename, threshold, context);
                databaseFacade.insert(model);
                segmentation.release();
            }

        }

        mask.release();
    }

    private RocModel createVisualization(String destination, Mat mask, Mat segmentation, String filename, int threshold, final RocWindowContext context) {
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

        return new RocModel(filename,
                threshold,
                falsePositive,
                falseNegative,
                truePositive,
                trueNegative,
                segmentation.width() * segmentation.height(),
                context.getColorReduce(),
                context.getDatabaseCollectionName());
    }

    private RocModel createWithoutVisualisation(Mat mask, Mat segmentation, String filename, int threshold, final RocWindowContext context) {
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

        return new RocModel(filename,
                threshold,
                falsePositive,
                falseNegative,
                truePositive,
                trueNegative,
                segmentation.width() * segmentation.height(),
                context.getColorReduce(),
                context.getDatabaseCollectionName());
    }

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
