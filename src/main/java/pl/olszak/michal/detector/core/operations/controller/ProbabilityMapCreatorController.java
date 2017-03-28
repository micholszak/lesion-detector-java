package pl.olszak.michal.detector.core.operations.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opencv.core.Mat;
import pl.olszak.michal.detector.core.operations.Operations;
import pl.olszak.michal.detector.core.operations.converter.ConvertedContainerCreator;
import pl.olszak.michal.detector.model.data.BayessianTable;
import pl.olszak.michal.detector.model.data.ColorProbabilityMap;
import pl.olszak.michal.detector.model.file.ImageType;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedContainer;
import pl.olszak.michal.detector.model.file.container.coverted.ConvertedFile;
import pl.olszak.michal.detector.model.file.container.image.ImageContainer;
import pl.olszak.michal.detector.utils.ColorReduce;
import pl.olszak.michal.detector.utils.DetectorFolders;
import pl.olszak.michal.detector.utils.FileOperations;
import pl.olszak.michal.detector.utils.Integers;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class ProbabilityMapCreatorController implements Controller {

    private final FileOperations fileOperations;
    private final ConvertedContainerCreator creator;
    private final Gson gson;

    public ProbabilityMapCreatorController(FileOperations fileOperations, Gson gson, ConvertedContainerCreator creator) {
        this.fileOperations = fileOperations;
        this.gson = gson;
        this.creator = creator;
    }

    @Override
    public void process() {
        for (ColorReduce reduce : ColorReduce.values()) {
            BayessianTable table = populateTable(reduce);
            ColorProbabilityMap colorProbabilityMap = createProbabilityMap(table);
            saveData(colorProbabilityMap);
        }
    }

    private BayessianTable populateTable(ColorReduce colorReduce) {
        BayessianTable table = new BayessianTable(colorReduce.getValue());
        ImageContainer colored = fileOperations.create(DetectorFolders.LEARNING_RESOURCES_FOLDER, ImageType.COLORED);
        ImageContainer mask = fileOperations.create(DetectorFolders.MASK_RESOURCES_FOLDER, ImageType.GRAYSCALE_MASK);

        ConvertedContainer coloredConverted = creator.createColoredContainer(colored.getImages());
        ConvertedContainer maskConverted = creator.createThresholded(mask.getImages(), 128, true);

        maskConverted.getConvertedFiles().entrySet().stream()
                .filter(entry -> coloredConverted.getConvertedFiles().containsKey(entry.getKey()))
                .forEach(entry -> {
                    ConvertedFile coloredFile = coloredConverted.getConvertedFiles().get(entry.getKey());
                    ConvertedFile maskFile = entry.getValue();

                    Mat coloredMat = coloredFile.getConverted();
                    Mat maskMat = maskFile.getConverted();

                    if (coloredMat.size().equals(maskMat.size())) {
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
                });
        return table;
    }

    private ColorProbabilityMap createProbabilityMap(BayessianTable table) {
        ColorProbabilityMap colorProbabilityMap = new ColorProbabilityMap(table.getColorMode());

        final long totalCount = table.getTotalCount();

        table.getLesionColors()
                .forEach((key, value) -> {
                    final long nonLesion = table.getNonLesionColors().getOrDefault(key, 0L);
                    final long lesion = value;

                    final double lesionProbability = Operations.calculateProbability(lesion, nonLesion, totalCount);
                    colorProbabilityMap.putLesionProbability(key, lesionProbability);
                    colorProbabilityMap.putNonLesionProbability(key, 1 - lesionProbability);
                });

        table.getNonLesionColors()
                .entrySet()
                .stream()
                .filter(entry -> !colorProbabilityMap.getNonLesionProbability().containsKey(entry.getKey()))
                .forEach(entry -> {
                    final long lesion = table.getLesionColors().getOrDefault(entry.getKey(), 0L);
                    final long nonLesion = entry.getValue();

                    final double lesionProbability = Operations.calculateProbability(lesion, nonLesion, totalCount);
                    colorProbabilityMap.putLesionProbability(entry.getKey(), lesionProbability);
                    colorProbabilityMap.putNonLesionProbability(entry.getKey(), 1 - lesionProbability);
                });


        return colorProbabilityMap;
    }

    private void saveData(ColorProbabilityMap colorProbabilityMap) {
        Type type = new TypeToken<ColorProbabilityMap>() {
        }.getType();
        String json = gson.toJson(colorProbabilityMap, type);

        DetectorFolders.saveJson(String.format(DetectorFolders.PROBABILITY_MAP, colorProbabilityMap.getColorMode()), json);
    }

}
