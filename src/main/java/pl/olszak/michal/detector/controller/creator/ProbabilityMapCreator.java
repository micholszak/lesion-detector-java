package pl.olszak.michal.detector.controller.creator;

import io.reactivex.Flowable;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pl.olszak.michal.detector.core.converter.ConvertedContainerCreator;
import pl.olszak.michal.detector.fx.scenes.database.DatabaseWindowContext;
import pl.olszak.michal.detector.model.data.BayessianTable;
import pl.olszak.michal.detector.model.data.ColorProbability;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;
import pl.olszak.michal.detector.model.file.container.image.ImageContainer;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.FileOperations;
import pl.olszak.michal.detector.utils.Integers;
import pl.olszak.michal.detector.utils.Operations;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class ProbabilityMapCreator implements MapCreator {

    private final Logger logger = LoggerFactory.getLogger(ProbabilityMapCreator.class);

    private final FileOperations fileOperations;
    private final ConvertedContainerCreator creator;
    private final AtomicInteger sampleSize = new AtomicInteger(0);

    @Autowired
    private MongoOperations mongo;

    public ProbabilityMapCreator(FileOperations fileOperations, ConvertedContainerCreator creator) {
        this.fileOperations = fileOperations;
        this.creator = creator;
    }

    @Override
    public void process(final ColorReduce colorReduce, final DatabaseWindowContext context) {
        logger.info("Started process");
        BayessianTable table = populateTable(colorReduce, context);
        createDatabase(table);
    }

    private BayessianTable populateTable(final ColorReduce colorReduce, final DatabaseWindowContext context) {
        BayessianTable table = new BayessianTable(colorReduce.getValue());
        ImageContainer colored = fileOperations.create(context.getImageResourcesFolder(), ImageType.COLORED);
        ImageContainer mask = fileOperations.create(context.getMaskFolder(), ImageType.GRAYSCALE_MASK);

        ConvertedContainer coloredConverted = creator.createColoredContainer(colored.getImages());
        ConvertedContainer maskConverted = creator.createThresholds(mask.getImages(), 128, true);

        Flowable.fromIterable(maskConverted.getConvertedFiles().entrySet())
                .filter(entry -> coloredConverted.getConvertedFiles().containsKey(entry.getKey()))
                .forEach(entry -> {
                    sampleSize.incrementAndGet();
                    addSample(colorReduce, table, coloredConverted, entry);
                });

        return table;
    }

    private void addSample(ColorReduce colorReduce, BayessianTable table, ConvertedContainer coloredConverted, Map.Entry<String, ConvertedFile> entry) {
        ConvertedFile coloredFile = coloredConverted.getConvertedFiles().get(entry.getKey());
        ConvertedFile maskFile = entry.getValue();

        logger.info(String.format("Processing %s", coloredFile.getImage().getFileName()));

        Mat coloredMat = coloredFile.getConverted();
        Mat maskMat = maskFile.getConverted();

        if (coloredMat.size().equals(maskMat.size())) {
            // TODO: 28.06.2017 przerzuć to do metody ?
            for (int row = 0; row < coloredMat.size().height; row++) {
                for (int col = 0; col < coloredMat.size().width; col++) {
                    byte[] colors = new byte[coloredMat.channels()];
                    coloredMat.get(row, col, colors);

                    int b = colorReduce.reduceChannels(Integers.unsignedInt(colors[0]));
                    int g = colorReduce.reduceChannels(Integers.unsignedInt(colors[1]));
                    int r = colorReduce.reduceChannels(Integers.unsignedInt(colors[2]));

                    byte[] maskValue = new byte[maskMat.channels()];
                    maskMat.get(row, col, maskValue);

                    if (Integers.unsignedInt(maskValue[0]) == 1) {
                        table.addLesionColor(new Color(r, g, b));
                    } else {
                        table.addNonLesionColor(new Color(r, g, b));
                    }
                }
            }
        }
    }

    private void createDatabase(BayessianTable table) {
        logger.info("Creating database ...");
        final long totalCount = table.getTotalCount();

        Flowable.fromIterable(table.getLesionColors().entrySet())
                .forEach(entry ->
                        insertProbability(table, totalCount, entry.getKey(), entry.getValue())
                );

        Flowable.fromIterable(table.getNonLesionColors().entrySet())
                .filter(entry -> {
                    final Criteria criteria = new Criteria("color").is(entry.getKey());
                    return !mongo.exists(new Query(criteria), ColorProbability.class);
                })
                .forEach(entry ->
                        insertProbability(table, totalCount, entry.getKey(), entry.getValue())
                );
        logger.info("Database creation finished");
    }

    private void insertProbability(BayessianTable table, long totalCount, Color color, Long value) {
        final long nonLesion = table.getNonLesionColors().getOrDefault(color, 0L);
        final long lesion = value;
        final double lesionProbability = Operations.calculateProbability(lesion, nonLesion, totalCount);
        ColorProbability colorProbability = new ColorProbability(color, table.getColorMode(), lesionProbability, 1 - lesionProbability, sampleSize.get());
        mongo.insert(colorProbability);
    }
}
