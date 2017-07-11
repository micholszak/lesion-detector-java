package pl.olszak.michal.detector.controller.segmentation;

import io.reactivex.Flowable;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.olszak.michal.detector.core.converter.ConvertedContainerCreator;
import pl.olszak.michal.detector.core.database.DatabaseFacade;
import pl.olszak.michal.detector.exception.FolderCreationException;
import pl.olszak.michal.detector.fx.scenes.segmentation.SegmentationWindowContext;
import pl.olszak.michal.detector.model.data.Color;
import pl.olszak.michal.detector.model.data.ColorProbability;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;
import pl.olszak.michal.detector.model.file.container.image.ImageContainer;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.ContainerOperations;
import pl.olszak.michal.detector.utils.FileOperationsUtils;
import pl.olszak.michal.detector.utils.Integers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
public class SegmentationController {

    private final Logger logger = LoggerFactory.getLogger(SegmentationController.class);

    private final ContainerOperations operations;
    private final ConvertedContainerCreator creator;
    private final DatabaseFacade databaseFacade;

    private static final int MAX_VALUE = 256;

    private static final String EXTENSION = ".bmp";

    public SegmentationController(ContainerOperations operations, ConvertedContainerCreator creator, DatabaseFacade databaseFacade) {
        this.operations = operations;
        this.creator = creator;
        this.databaseFacade = databaseFacade;
    }

    public void createSegmentedImages(final ColorReduce colorReduce, final SegmentationWindowContext context) {
        if (databaseFacade.probabilityDatabaseExists()) {
            try {
                String folderPath = FileOperationsUtils.createDestinationFolder(colorReduce, context.getSegmentationDestinationFolder());
                createSegmenattionResults(folderPath, context.getSegmentationResourcesFolder(), colorReduce);
            } catch (FolderCreationException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void createSegmenattionResults(String destinationPath, String resourcesPath, ColorReduce colorReduce) {
        ImageContainer segmentationSources = operations.create(resourcesPath, ImageType.COLORED);
        ConvertedContainer sources = creator.createColoredContainer(segmentationSources.getImages());
        final Map<Color, Double> cacheMap = new HashMap<>();

        Flowable.fromIterable(sources.getConvertedFiles().entrySet())
                .forEach(entry -> processImage(entry.getKey(), entry.getValue(), destinationPath, colorReduce, cacheMap));
    }

    private void processImage(String filename, ConvertedFile convertedFile, String destinationPath, ColorReduce colorReduce, Map<Color, Double> cacheMap) {
        logger.info(String.format("Segmentation started on: %s, with colorReduce %d", filename, colorReduce.getValue()));
        Mat colored = convertedFile.getConverted();
        byte segmentedBytes[] = new byte[(int) (colored.size().height * colored.size().width * CvType.CV_8S)];

        for (int row = 0; row < colored.height(); row++) {
            for (int col = 0; col < colored.width(); col++) {
                final int index = row * colored.width() + col;
                byte[] colors = new byte[colored.channels()];
                colored.get(row, col, colors);

                int b = colorReduce.reduceChannels(Integers.unsignedInt(colors[0]));
                int g = colorReduce.reduceChannels(Integers.unsignedInt(colors[1]));
                int r = colorReduce.reduceChannels(Integers.unsignedInt(colors[2]));

                Color color = new Color(r, g, b);
                cacheMap(cacheMap, color, colorReduce);
                segmentedBytes[index] = (byte) (cacheMap.get(color) * MAX_VALUE);
            }
        }

        Mat mat = new Mat(colored.size(), CvType.CV_8U);
        mat.put(0, 0, segmentedBytes);

        File file = new File(destinationPath, filename + EXTENSION);
        Imgcodecs.imwrite(file.getAbsolutePath(), mat);
    }

    private void cacheMap(Map<Color, Double> cacheMap, Color color, ColorReduce colorReduce) {
        if (!cacheMap.containsKey(color)) {
            Optional<ColorProbability> optional = databaseFacade.retrieve(color, colorReduce);
            if (optional.isPresent()) {
                cacheMap.put(color, optional.get().getLesionProbability());
            } else {
                cacheMap.put(color, 0.0);
            }
        }
    }
}
