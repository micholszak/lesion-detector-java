package pl.olszak.michal.detector.model.data;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.olszak.michal.detector.utils.ColorReduce;

import java.util.Set;

/**
 * @author molszak
 * created on 12.07.2017.
 */
@Document(collection = "roc")
public class RocDataSet {

    @Indexed(unique = true)
    private final String name;

    private final ColorReduce colorMode;

    private final Set<RocModel> rocCollection;

    public RocDataSet(
            String name,
            ColorReduce colorMode,
            Set<RocModel> rocCollection) {

        this.name = name;
        this.colorMode = colorMode;
        this.rocCollection = rocCollection;
    }

    public String getName() {
        return name;
    }

    public Set<RocModel> getRocCollection() {
        return rocCollection;
    }

    public ColorReduce getColorMode() {
        return colorMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RocDataSet that = (RocDataSet) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (colorMode != that.colorMode) return false;

        return rocCollection != null ? rocCollection.equals(that.rocCollection) : that.rocCollection == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (colorMode != null ? colorMode.hashCode() : 0);
        result = 31 * result + (rocCollection != null ? rocCollection.hashCode() : 0);
        return result;
    }
}
