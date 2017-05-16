package pl.olszak.michal.detector.utils;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class Operations {

    private static final double FACTOR = 0.5;

    public static double calculateProbability(long lesion, long nonLesion, long total) {
        final double lesionProbability = (double) lesion / (double) total;
        final double nonLesionProbability = (double) nonLesion / (double) total;

        return (lesionProbability * FACTOR) / ((lesionProbability * FACTOR) + (nonLesionProbability * FACTOR));
    }

}
