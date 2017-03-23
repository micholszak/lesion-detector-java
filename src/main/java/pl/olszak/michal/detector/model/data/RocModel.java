package pl.olszak.michal.detector.model.data;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
public class RocModel {

    private final String fileName;
    private final int threshold;
    private final int falsePositive;
    private final int falseNegative;
    private final int truePositive;
    private final int trueNegative;
    private final int totalCount;

    public RocModel(String fileName,
                    int threshold,
                    int falsePositive,
                    int falseNegative,
                    int truePositive,
                    int trueNegative,
                    int totalCount) {
        this.fileName = fileName;
        this.threshold = threshold;
        this.falsePositive = falsePositive;
        this.falseNegative = falseNegative;
        this.truePositive = truePositive;
        this.trueNegative = trueNegative;
        this.totalCount = totalCount;
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

        return fileName != null ? fileName.equals(rocModel.fileName) : rocModel.fileName == null;
    }

    @Override
    public int hashCode() {
        int result = fileName != null ? fileName.hashCode() : 0;
        result = 31 * result + threshold;
        result = 31 * result + falsePositive;
        result = 31 * result + falseNegative;
        result = 31 * result + truePositive;
        result = 31 * result + trueNegative;
        result = 31 * result + totalCount;
        return result;
    }
}
