package pl.olszak.michal.detector.utils;

import java.util.Locale;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public enum ColorReduce {

    BINS_PER_CHANNEL_4(4),
    BINS_PER_CHANNEL_8(8),
    BINS_PER_CHANNEL_16(16),
    BINS_PER_CHANNEL_32(32),
    BINS_PER_CHANNEL_64(64),
    BINS_PER_CHANNEL_128(128),
    BINS_PER_CHANNEL_256(256);

    private static final int MAX_VALUE = 256;

    private final int value;

    ColorReduce(int value) {
        this.value = value;
    }

    public static ColorReduce of(int value) {
        for (ColorReduce colorReduce : values()) {
            if (colorReduce.value == value) {
                return colorReduce;
            }
        }
        return BINS_PER_CHANNEL_256;
    }

    public int reduceChannels(int channel) {
        int multiplier = MAX_VALUE / value;
        if (channel < (multiplier * (value - 1))) {
            return (int) (channel / multiplier + 0.5) * multiplier;
        }
        return MAX_VALUE - 1;
    }

    public int getValue() {
        return value;
    }


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Reduction %d", getValue());
    }
}
