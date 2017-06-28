package pl.olszak.michal.detector.controller.segmentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import pl.olszak.michal.detector.model.data.ColorProbability;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
public class SegmentationController {

    private final Logger logger = LoggerFactory.getLogger(SegmentationController.class);

    @Autowired
    private MongoOperations mongo;

    public void executeTest(){
        // TODO: 28.06.2017 dorobić w domu, nie do końca wiem co chcę tu osiągnąć
       if(mongo.collectionExists(ColorProbability.class)){
           // TODO: 28.06.2017 zbinaryzować obrazy do testów (potrzebujesz contextu)
           // TODO: 28.06.2017 zredukować kanały
           // TODO: 28.06.2017 kolor chyba nie może być id, co jeżeli w zredukowanych kanałach pojawi się ten sam kolor
           // TODO: 28.06.2017 wyszukać koloru w bazie danych, porównać kolor i dodać wynik, pytanie czy opłaca się cachować kolekcje kolorów z bazy danych czy wyciągać jak potrzebuję kolor
           // TODO: 28.06.2017 zapisać zesgmentowany obraz, dodać różne możliwości segmentacji (różne metody)
       }
    }

    /*
    public void executeTest(BayessianProbabilityMap map) {
        if (!map.getLesionProbability().isEmpty()) {
            ImageContainer coloredImageContainer = operations.createContainer(Utils.getTestFolder(), ImageTypes.COLORED);
            CollectionBinarizer collectionBinarizer = new RegularCollectionBinarizer(coloredImageContainer, ImageTypes.COLORED);
            Container coloredBinarizedContainer = collectionBinarizer.binarizeCollection();

            final Map<Color, Double> lesionMap = map.getLesionProbability();

            final Map<String, byte[]> coloredContainer = ((BinarizedContainer) coloredBinarizedContainer).getBinarizedContainer();
            coloredContainer.entrySet().forEach(entry -> {
                byte[] coloredBytes = entry.getValue();
                byte[] bytes = new byte[coloredBytes.length / 3];

                for (int i = 0; i < bytes.length; i++) {
                    int b = IntUtils.getUnsignedInt(coloredBytes[i * 3]);
                    int g = IntUtils.getUnsignedInt(coloredBytes[i * 3 + 1]);
                    int r = IntUtils.getUnsignedInt(coloredBytes[i * 3 + 2]);

                    ColorReduce reduce = ColorReduce.of(map.getBins());
                    if (reduce != ColorReduce.BINS_PER_CHANNEL_FULL) {
                        b = IntUtils.reduceChannels(b, reduce);
                        g = IntUtils.reduceChannels(g, reduce);
                        r = IntUtils.reduceChannels(r, reduce);
                    }

                    Color color = new Color(r, g, b);
                    bytes[i] = (byte) Math.round((lesionMap.containsKey(color) ? lesionMap.get(color) : 0) * MAX_VALUE);
                }

                Mat mat = new Mat(512, 768, CvType.CV_8U);
                mat.put(0, 0, bytes);

                File file = new File(Utils.getResultsFolder(), entry.getKey() + BMP_EXTENSION);
                Highgui.imwrite(file.getAbsolutePath(), mat);
            });
        }

    }
     */
}
