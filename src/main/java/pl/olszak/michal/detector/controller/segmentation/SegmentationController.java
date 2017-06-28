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
       }
    }
}
