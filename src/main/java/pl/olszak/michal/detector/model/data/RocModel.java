package pl.olszak.michal.detector.model.data;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.olszak.michal.detector.utils.ColorReduce;

/**
 * @author molszak
 * created on 23.03.2017.
 */
@CompoundIndexes({
        @CompoundIndex(name = "colorInMode", def = "{'colorMode': 1}")
})
@Document(collection = "roc")
public class RocModel {

    private final String fileName;
    private final int threshold;
    private final int falsePositive;
    private final int falseNegative;
    private final int truePositive;
    private final int trueNegative;
    private final int totalCount;
    private final ColorReduce colorMode;
    private final String name;

    public RocModel(
            String fileName,
            int threshold,
            int falsePositive,
            int falseNegative,
            int truePositive,
            int trueNegative,
            int totalCount,
            ColorReduce colorMode,
            String name) {

        this.fileName = fileName;
        this.threshold = threshold;
        this.falsePositive = falsePositive;
        this.falseNegative = falseNegative;
        this.truePositive = truePositive;
        this.trueNegative = trueNegative;
        this.totalCount = totalCount;
        this.colorMode = colorMode;
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public int getThreshold() {
        return threshold;
    }

    public int getFalsePositive() {
        return falsePositive;
    }

    public int getFalseNegative() {
        return falseNegative;
    }

    public int getTruePositive() {
        return truePositive;
    }

    public int getTrueNegative() {
        return trueNegative;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ColorReduce getColorMode() {
        return colorMode;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RocModel rocModel = (RocModel) o;

        if (threshold != rocModel.threshold) return false;
        if (falsePositive != rocModel.falsePositive) return false;
        if (falseNegative != rocModel.falseNegative) return false;
        if (truePositive != rocModel.truePositive) return false;
        if (trueNegative != rocModel.trueNegative) return false;
        if (totalCount != rocModel.totalCount) return false;
        if (colorMode != rocModel.colorMode) return false;
        if (!name.equals(rocModel.name)) return false;

        return fileName != null ? fileName.equals(rocModel.fileName) : rocModel.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + threshold;
        result = 31 * result + falsePositive;
        result = 31 * result + falseNegative;
        result = 31 * result + truePositive;
        result = 31 * result + trueNegative;
        result = 31 * result + totalCount;
        result = 31 * result + colorMode.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
