package pl.olszak.michal.detector.core.database;

import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pl.olszak.michal.detector.model.data.Color;
import pl.olszak.michal.detector.model.data.ColorProbability;
import pl.olszak.michal.detector.model.data.RocDataSet;
import pl.olszak.michal.detector.utils.ColorReduce;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author molszak
 *         created on 30.06.2017.
 */
public class DatabaseFacade {

    private MongoOperations mongo;

    public DatabaseFacade(MongoOperations mongo) {
        this.mongo = mongo;
    }

    private static Criteria createCriteriaColorProbability(Color color, ColorReduce reduce) {
        return new Criteria("color").is(color).and("colorMode").is(reduce.getValue());
    }

    private static Criteria createCriteriaRocData(ColorReduce reduce) {
        return new Criteria("colorMode").is(reduce.getValue());
    }

    private static Criteria createCriteriaRocData(String name) {
        return new Criteria("name").is(name);
    }

    public boolean probabilityExists(Color color, ColorReduce reduce) {
        final Criteria criteria = createCriteriaColorProbability(color, reduce);
        return mongo.exists(new Query(criteria), ColorProbability.class);
    }

    public boolean rocExists(String name) {
        final Criteria criteria = createCriteriaRocData(name);
        return mongo.exists(new Query(criteria), RocDataSet.class);
    }

    public Optional<ColorProbability> retrieve(Color color, ColorReduce reduce) {
        final Criteria criteria = createCriteriaColorProbability(color, reduce);
        ColorProbability probability = mongo.findOne(new Query(criteria), ColorProbability.class);
        return Optional.ofNullable(probability);
    }

    /**
     * Inserts an colorProbability to collection named "probabilityMap",
     * does not care for uniqueness of data, so it's advised to check data using {@link #probabilityExists(Color, ColorReduce)}
     *
     * @param colorProbability probability that should be inserted into database
     */
    public void insert(ColorProbability colorProbability) {
        mongo.insert(colorProbability);
    }

    /**
     * Inserts data set to collection named "roc", does care about uniqueness of name field
     *
     * @param dataSet data set to be inserted into collection
     */
    public void insert(RocDataSet dataSet) {
        mongo.insert(dataSet);
    }

    public Optional<RocDataSet> retrieveSingle(String name) {
        RocDataSet set = mongo.findById(name, RocDataSet.class);
        return Optional.ofNullable(set);
    }

    public List<RocDataSet> retrieveAll() {
        List<RocDataSet> dataSets = new ArrayList<>();
        dataSets.addAll(mongo.findAll(RocDataSet.class));
        return dataSets;
    }

    public List<RocDataSet> retrieveByColorReduction(ColorReduce reduce) {
        List<RocDataSet> dataSets = new ArrayList<>();
        final Criteria criteria = createCriteriaRocData(reduce);
        dataSets.addAll(mongo.find(new Query(criteria), RocDataSet.class));
        return dataSets;
    }

    public boolean removeRoc(String name) {
        if (rocExists(name)) {
            WriteResult result = mongo.remove(new Query(createCriteriaRocData(name)), RocDataSet.class);
            if (result.getN() == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean probabilityDatabaseExists() {
        return mongo.collectionExists(ColorProbability.class);
    }

}
