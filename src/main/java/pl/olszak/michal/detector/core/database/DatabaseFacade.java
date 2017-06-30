package pl.olszak.michal.detector.core.database;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pl.olszak.michal.detector.model.data.Color;
import pl.olszak.michal.detector.model.data.ColorProbability;
import pl.olszak.michal.detector.utils.ColorReduce;

import java.util.Optional;


/**
 * @author molszak
 *         created on 30.06.2017.
 */
public class DatabaseFacade {

    private MongoOperations mongo;

    public DatabaseFacade(MongoOperations mongo){
        this.mongo = mongo;
    }

    private static Criteria createCriteria(Color color, ColorReduce reduce){
        return new Criteria("color").is(color).and("colorMode").is(reduce.getValue());
    }

    public boolean probabilityExistst(Color color, ColorReduce reduce){
        final Criteria criteria = createCriteria(color, reduce);
        return mongo.exists(new Query(criteria), ColorProbability.class);
    }

    public Optional<ColorProbability> retrieve(Color color, ColorReduce reduce){
        final Criteria criteria = createCriteria(color, reduce);
        ColorProbability probability = mongo.findOne(new Query(criteria), ColorProbability.class);
        return Optional.ofNullable(probability);
    }

    /**
     * Inserts an colorProbability to collection named "probabilityMap",
     * does not care for uniqueness of data, so it's advised to check data using {@link #probabilityExistst(Color, ColorReduce)}
     * @param colorProbability probability that should be inserted into database
     */
    public void insert(ColorProbability colorProbability){
        mongo.insert(colorProbability);
    }

    public boolean probabilityDatabaseExists(){
        return mongo.collectionExists(ColorProbability.class);
    }

}
