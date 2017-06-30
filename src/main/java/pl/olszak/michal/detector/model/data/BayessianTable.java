package pl.olszak.michal.detector.model.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
public class BayessianTable {

    private final int colorMode;
    private final Map<Color, Long> lesionColors;
    private final Map<Color, Long> nonLesionColors;
    private Long totalCount;

    public BayessianTable(int colorMode) {
        this.colorMode = colorMode;
        this.lesionColors = new HashMap<>();
        this.nonLesionColors = new HashMap<>();
        this.totalCount = 0L;
    }

    public int getColorMode() {
        return colorMode;
    }

    public Map<Color, Long> getLesionColors() {
        return lesionColors;
    }

    public Map<Color, Long> getNonLesionColors() {
        return nonLesionColors;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public synchronized void addLesionColor(Color color) {
        Long count = lesionColors.getOrDefault(color, 0L);
        count++;
        lesionColors.put(color, count);
        totalCount++;
    }

    public synchronized void addNonLesionColor(Color color) {
        Long count = nonLesionColors.getOrDefault(color, 0L);
        count++;
        nonLesionColors.put(color, count);
        totalCount++;
    }
}
