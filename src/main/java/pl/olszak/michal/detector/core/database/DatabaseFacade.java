package pl.olszak.michal.detector.core.database;

import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pl.olszak.michal.detector.model.data.Color;
import pl.olszak.michal.detector.model.data.ColorProbability;
import pl.olszak.michal.detector.model.data.RocDataSet;
import pl.olszak.michal.detector.model.data.RocModel;
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

    private static Criteria createCriteriaRocModel(ColorReduce reduce) {
        return new Criteria("colorMode").is(reduce.getValue());
    }

    private static Criteria createCriteriaRocModel(String name, ColorReduce colorMode) {
        return new Criteria("name").is(name).and("colorMode").is(colorMode);
    }

    public boolean probabilityExists(Color color, ColorReduce reduce) {
        final Criteria criteria = createCriteriaColorProbability(color, reduce);
        return mongo.exists(new Query(criteria), ColorProbability.class);
    }

    public boolean rocExists(String name, ColorReduce colorMode) {
        final Criteria criteria = createCriteriaRocModel(name, colorMode);
        return mongo.exists(new Query(criteria), RocModel.class);
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
     * @param model model to be inserted into collection
     */
    public void insert(RocModel model){
        mongo.insert(model);
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
        final Criteria criteria = createCriteriaRocModel(reduce);
        dataSets.addAll(mongo.find(new Query(criteria), RocDataSet.class));
        return dataSets;
    }

    public boolean removeRoc(String name, ColorReduce colorMode) {
        if (rocExists(name, colorMode)) {
            WriteResult result = mongo.remove(new Query(createCriteriaRocModel(name, colorMode)), RocDataSet.class);
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
