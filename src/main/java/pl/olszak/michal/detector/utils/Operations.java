package pl.olszak.michal.detector.utils;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class Operations {

    public enum RocColor {
        RED,
        BLUE,
        BLACK,
        WHITE
    }

    private static final double FACTOR = 0.5;

    public static double calculateProbability(long lesion, long nonLesion, long total) {
        final double lesionProbability = (double) lesion / (double) total;
        final double nonLesionProbability = (double) nonLesion / (double) total;

        return (lesionProbability * FACTOR) / ((lesionProbability * FACTOR) + (nonLesionProbability * FACTOR));
    }

    /**
     * Colors are reversed for the sake of Opencv bgr convention
     * @param color
     * @return
     */
    public static byte[] createColor(RocColor color) {
        switch (color) {
            case RED:
                return new byte[]{0, 0, (byte) 255};
            case BLUE:
                return new byte[]{(byte) 255, 0, 0};
            case BLACK:
                return new byte[]{0, 0, 0};
            case WHITE:
            default:
                return new byte[]{(byte) 255, (byte) 255, (byte) 255};
        }
    }

}
