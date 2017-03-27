package pl.olszak.michal.detector.model.data;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class ColorProbabilityMap {

    private final int colorMode;
    private final Map<Color, Double> lesionProbability;
    private final Map<Color, Double> nonLesionProbability;

    public ColorProbabilityMap(int colorMode) {
        this.colorMode = colorMode;
        this.lesionProbability = new HashMap<>();
        this.nonLesionProbability = new HashMap<>();
    }

    public int getColorMode() {
        return colorMode;
    }

    public Map<Color, Double> getLesionProbability() {
        return lesionProbability;
    }

    public Map<Color, Double> getNonLesionProbability() {
        return nonLesionProbability;
    }

    public void putLesionProbability(Color color, Double probability) {
        lesionProbability.put(color, probability);
    }

    public void putNonLesionProbability(Color color, Double probability) {
        nonLesionProbability.put(color, probability);
    }

}
